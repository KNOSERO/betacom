package com.ravcube.betacom.api.dto;

import java.time.Instant;
import java.util.UUID;

public record ShareItemResponse(UUID itemId, UUID userId, ShareRole role, Instant grantedAt) {
}
