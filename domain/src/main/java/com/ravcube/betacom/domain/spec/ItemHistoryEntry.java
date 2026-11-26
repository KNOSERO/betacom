package com.ravcube.betacom.domain.spec;

import java.time.Instant;
import java.util.UUID;

public record ItemHistoryEntry(
        int revision,
        String revisionType,
        Instant timestamp,
        UUID createdBy,
        String title,
        String content) {
}
