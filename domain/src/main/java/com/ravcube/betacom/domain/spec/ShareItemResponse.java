package com.ravcube.betacom.domain.spec;

import java.time.Instant;
import java.util.UUID;

public record ShareItemResponse(UUID itemId, UUID userId, ShareRole role, Instant grantedAt, Boolean created) {

}

