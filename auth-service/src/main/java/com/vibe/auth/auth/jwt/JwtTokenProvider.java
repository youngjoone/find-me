package com.vibe.auth.auth.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpirationMs; // in milliseconds

    @Value("${jwt.refresh-expiration}")
    private long jwtRefreshExpirationMs; // in milliseconds

    @Value("${jwt.issuer}")
    private String jwtIssuer;

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String createAccessToken(String email, Set<String> roles) {
        return Jwts.builder()
                .setSubject(email)
                .claim("roles", roles.stream().collect(Collectors.joining(",")))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .setIssuer(jwtIssuer)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtRefreshExpirationMs))
                .setIssuer(jwtIssuer)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Long getExpirationTime() {
        return jwtExpirationMs;
    }
}
