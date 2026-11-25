package com.ravcube.betacom.api.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record ShareItemRequest(@NotNull UUID userId, @NotNull ShareRole role) {
}
