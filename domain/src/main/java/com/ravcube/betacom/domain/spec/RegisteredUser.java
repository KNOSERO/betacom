package com.ravcube.betacom.domain.spec;

import java.time.Instant;
import java.util.UUID;

public record RegisteredUser(UUID userId, String login, Instant createdAt) {
}
