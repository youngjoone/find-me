package com.vibe.auth.auth.presentation;

import com.vibe.auth.auth.jwt.JwtTokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class JwksController {

    private final JwtTokenProvider jwtTokenProvider;

    public JwksController(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("/.well-known/jwks.json")
    public ResponseEntity<Map<String, Object>> getJwks() {
        // This is a placeholder. In a real application, you would expose the public key in JWK format.
        // For HS256, JWKS is not typically used as there's no public key to expose.
        // The spec mentions RSA/ECDSA, which requires a different JWT implementation.
        return ResponseEntity.ok(Map.of("keys", new Object[]{}));
    }
}
