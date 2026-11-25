package com.ravcube.betacomp.api.dto;

import java.time.Instant;

public record ItemHistoryEntryResponse(
        int revision,
        String revisionType,
        Instant timestamp,
        String changedBy,
        String title,
        String content) {
}
