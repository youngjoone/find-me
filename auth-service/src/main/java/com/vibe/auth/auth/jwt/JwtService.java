
package com.vibe.auth.auth.jwt;

import com.vibe.auth.config.JwtProperties;
import com.vibe.auth.config.RsaKeyLoader;
import com.vibe.auth.user.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.security.PublicKey;
import java.time.Instant;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtService {

    private final JwtProperties jwtProperties;
    private final RsaKeyLoader rsaKeyLoader;

    public JwtService(JwtProperties jwtProperties, RsaKeyLoader rsaKeyLoader) {
        this.jwtProperties = jwtProperties;
        this.rsaKeyLoader = rsaKeyLoader;
    }

    private Key getSigningKey() {
        return rsaKeyLoader.getPrivateKey();
    }

    public PublicKey getVerificationKey() {
        return rsaKeyLoader.getPublicKey();
    }

    public String createAccessToken(String email, Set<String> roles) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("roles", roles.stream().collect(Collectors.joining(",")));
        claims.setIssuer(jwtProperties.getIssuer());
        claims.setAudience(jwtProperties.getAudience());
        claims.setIssuedAt(new Date());
        claims.setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getAccessTtlSec() * 1000));
        claims.setId(UUID.randomUUID().toString()); // JTI for Access Token

        return Jwts.builder()
                .setClaims(claims)
                .signWith(getSigningKey(), SignatureAlgorithm.RS256)
                .compact();
    }

    public String createRefreshToken(User user) {
        UUID jti = UUID.randomUUID();
        Instant expiresAt = Instant.now().plusSeconds(jwtProperties.getRefreshTtlSec());

        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(Date.from(expiresAt))
                .setIssuer(jwtProperties.getIssuer())
                .setAudience(jwtProperties.getAudience())
                .setId(jti.toString()) // JTI for Refresh Token
                .signWith(getSigningKey(), SignatureAlgorithm.RS256)
                .compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getVerificationKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getEmailFromToken(String token) {
        return parseToken(token).getSubject();
    }

    public Set<String> getRolesFromToken(String token) {
        String rolesString = (String) parseToken(token).get("roles");
        return Set.of(rolesString.split(","));
    }

    public Long getAccessTokenExpirationTime() {
        return jwtProperties.getAccessTtlSec() * 1000; // in milliseconds
    }

    public String getKid() {
        // For JWKS, we need a Key ID. For simplicity, we'll use a fixed one or derive from public key.
        // In a real app, this would be managed more robustly.
        return "vibe-auth-key-1"; // Fixed KID for now
    }
}
