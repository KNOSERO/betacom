package com.ravcube.betacom.api.dto;

import java.util.UUID;

public record ShareItemRequest(UUID userId, ShareRole role) {
}
