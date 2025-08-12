package com.vibe.auth.auth.presentation;

import com.vibe.auth.auth.application.AuthService;
import com.vibe.auth.auth.dto.LoginRequest;
import com.vibe.auth.auth.dto.LoginResponse;
import com.vibe.auth.auth.dto.RegisterRequest;
import com.vibe.auth.user.domain.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody RegisterRequest request) {
        User registeredUser = authService.register(request);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Auth service is up and running!");
    }
}
