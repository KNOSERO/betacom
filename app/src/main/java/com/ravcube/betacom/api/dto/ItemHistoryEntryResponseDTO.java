package com.ravcube.betacom.api.dto;

import java.time.Instant;
import java.util.UUID;

public record ItemHistoryEntryResponseDTO(
        int revision,
        String revisionType,
        Instant timestamp,
        UUID createdBy,
        String title,
        String content) {
}
