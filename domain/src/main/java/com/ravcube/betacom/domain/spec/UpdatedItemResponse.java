package com.ravcube.betacom.domain.spec;

import java.time.Instant;
import java.util.UUID;

public record UpdatedItemResponse(
        UUID id,
        String title,
        String content,
        Integer version,
        Instant updatedAt) {
}