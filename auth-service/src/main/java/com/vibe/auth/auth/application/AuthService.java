package com.vibe.auth.auth.application;

import com.vibe.auth.user.domain.User;
import com.vibe.auth.user.repository.UserRepository;
import com.vibe.auth.auth.dto.RegisterRequest;
import com.vibe.auth.auth.dto.LoginRequest;
import com.vibe.auth.auth.dto.LoginResponse;
import com.vibe.auth.auth.jwt.JwtTokenProvider;
import com.vibe.auth.common.exception.DuplicateEmailException; // Import
import com.vibe.auth.common.exception.InvalidCredentialsException; // Import
import com.vibe.auth.common.exception.UserNotFoundException; // Import
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Transactional
    public User register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException("Email already exists"); // Use custom exception
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPwHash(passwordEncoder.encode(request.getPassword()));
        user.setNickname(request.getNickname());
        user.setRoles(new HashSet<>(Collections.singletonList("ROLE_USER"))); // Default role

        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found")); // Use custom exception

        if (!passwordEncoder.matches(request.getPassword(), user.getPwHash())) {
            throw new InvalidCredentialsException("Invalid credentials"); // Use custom exception
        }

        String accessToken = jwtTokenProvider.createAccessToken(user.getEmail(), user.getRoles());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getEmail());

        return new LoginResponse(accessToken, refreshToken, jwtTokenProvider.getExpirationTime());
    }
}
