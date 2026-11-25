package com.ravcube.betacomp.api.dto;

import java.time.Instant;
import java.util.UUID;

public record ShareItemResponse(UUID itemId, UUID userId, ShareRole role, Instant grantedAt) {
}
