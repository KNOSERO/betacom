package com.ravcube.betacomp.api.dto;

import java.time.Instant;
import java.util.UUID;

public record ItemListResponse(
        UUID id,
        String title,
        String content,
        Integer version,
        UUID ownerId,
        ItemRole myRole,
        Instant updatedAt) {
}
