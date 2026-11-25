package com.ravcube.betacom.api.dto;

import java.time.Instant;
import java.util.UUID;

public record UpdatedItemResponse(
        UUID id,
        String title,
        String content,
        Integer version,
        Instant updatedAt) {
}
