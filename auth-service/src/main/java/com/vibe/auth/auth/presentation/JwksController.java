
package com.vibe.auth.auth.presentation;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.vibe.auth.auth.jwt.JwtService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.interfaces.RSAPublicKey;
import java.util.Collections;

@RestController
public class JwksController {

    private final JwtService jwtService;

    public JwksController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @GetMapping("/.well-known/jwks.json")
    public java.util.Map<String, Object> jwks() {
        RSAPublicKey publicKey = (RSAPublicKey) jwtService.getVerificationKey();
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .keyID(jwtService.getKid())
                .build();
        return new JWKSet(rsaKey).toJSONObject();
    }
}
