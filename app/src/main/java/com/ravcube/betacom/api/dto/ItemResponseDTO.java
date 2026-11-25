package com.ravcube.betacom.api.dto;

import java.time.Instant;
import java.util.UUID;

public record ItemResponseDTO(
        UUID id,
        String title,
        String content,
        Integer version,
        UUID ownerId,
        Instant createdAt,
        Instant updatedAt) {
}
