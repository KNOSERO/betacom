package com.ravcube.betacom.security.jwt;

import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security.jwt")
public record JwtSecurityProperties(String secret, Duration expiration) {
}
