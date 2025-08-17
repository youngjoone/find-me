
package com.vibe.auth.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")
@Getter
@Setter
public class JwtProperties {
    private String issuer;
    private String audience;
    private String secret;
    private Long expiration;
    private Long refreshExpiration;

    // Access Token TTL in seconds
    public Long getAccessTtlSec() {
        return expiration / 1000;
    }

    // Refresh Token TTL in seconds
    public Long getRefreshTtlSec() {
        return refreshExpiration / 1000;
    }
}
