package com.ravcube.betacomp.api.dto;

import java.util.UUID;

public record ShareItemRequest(UUID userId, ShareRole role) {
}
