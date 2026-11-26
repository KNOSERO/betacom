package com.ravcube.betacom.api.dto;

import java.time.Instant;
import java.util.UUID;

public record RegisterResponseDTO(UUID id, String login, Instant createdAt) {
}
