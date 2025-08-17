package com.vibe.auth.auth.application;

import com.vibe.auth.auth.domain.RefreshToken;
import com.vibe.auth.auth.dto.LoginRequest;
import com.vibe.auth.auth.dto.RegisterRequest;
import com.vibe.auth.auth.dto.TokenResponse;
import com.vibe.auth.auth.jwt.JwtService;
import com.vibe.auth.auth.repository.RefreshTokenRepository;
import com.vibe.auth.common.exception.DuplicateEmailException;
import com.vibe.auth.common.exception.InvalidCredentialsException;
import com.vibe.auth.user.domain.User;
import com.vibe.auth.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;
import java.util.Set;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, RefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Transactional
    public User register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException("Email already exists");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getNickname())
                .roles("USER")
                .build();

        return userRepository.save(user);
    }

    @Transactional
    public TokenResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        String accessToken = jwtService.createAccessToken(user.getEmail(), Set.of(user.getRoles().split(",")));
        String refreshTokenString = jwtService.createRefreshToken(user);

        // Parse JTI and expiration from the token string
        Claims claims = jwtService.parseToken(refreshTokenString);
        UUID jti = UUID.fromString(claims.getId());
        Instant expiresAt = claims.getExpiration().toInstant();

        RefreshToken refreshToken = new RefreshToken(user, hashToken(refreshTokenString), jti, expiresAt);
        refreshTokenRepository.save(refreshToken);

        return new TokenResponse(accessToken, refreshTokenString, jwtService.getAccessTokenExpirationTime());
    }

    @Transactional
    public TokenResponse refresh(String refreshTokenString) {
        Claims claims = jwtService.parseToken(refreshTokenString);
        UUID jti = UUID.fromString(claims.getId());
        String userEmail = claims.getSubject();

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new TokenRefreshException(refreshTokenString, "User not found"));

        RefreshToken storedRefreshToken = refreshTokenRepository.findByTokenHash(hashToken(refreshTokenString))
                .orElseThrow(() -> new TokenRefreshException(refreshTokenString, "Refresh token not found"));

        if (storedRefreshToken.isRevoked() || storedRefreshToken.getExpiresAt().isBefore(Instant.now())) {
            throw new TokenRefreshException(refreshTokenString, "Refresh token is revoked or expired");
        }

        // Revoke old token
        storedRefreshToken.revoke();
        refreshTokenRepository.save(storedRefreshToken);

        // Generate new tokens
        String newAccessToken = jwtService.createAccessToken(user.getEmail(), Set.of(user.getRoles().split(",")));
        String newRefreshTokenString = jwtService.createRefreshToken(user);

        // Save new refresh token
        Claims newClaims = jwtService.parseToken(newRefreshTokenString);
        UUID newJti = UUID.fromString(newClaims.getId());
        Instant newExpiresAt = newClaims.getExpiration().toInstant();
        RefreshToken newRefreshToken = new RefreshToken(user, hashToken(newRefreshTokenString), newJti, newExpiresAt);
        refreshTokenRepository.save(newRefreshToken);

        return new TokenResponse(newAccessToken, newRefreshTokenString, jwtService.getAccessTokenExpirationTime());
    }

    @Transactional
    public void logout(String refreshTokenString) {
        RefreshToken storedRefreshToken = refreshTokenRepository.findByTokenHash(hashToken(refreshTokenString))
                .orElseThrow(() -> new TokenRefreshException(refreshTokenString, "Refresh token not found"));

        storedRefreshToken.revoke();
        refreshTokenRepository.save(storedRefreshToken);
    }

    private String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Could not hash token", e);
        }
    }
}