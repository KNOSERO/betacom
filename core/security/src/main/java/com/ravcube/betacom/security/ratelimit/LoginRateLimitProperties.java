package com.ravcube.betacom.security.ratelimit;

import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security.rate-limit.login")
public record LoginRateLimitProperties(long capacity, Duration window) {

    public LoginRateLimitProperties {
        if (capacity < 1) {
            throw new IllegalArgumentException("security.rate-limit.login.capacity must be positive");
        }
        if (window == null || window.isZero() || window.isNegative()) {
            throw new IllegalArgumentException("security.rate-limit.login.window must be positive");
        }
    }
}
