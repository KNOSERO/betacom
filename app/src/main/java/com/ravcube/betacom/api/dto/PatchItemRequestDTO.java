package com.ravcube.betacom.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PatchItemRequestDTO(
        @Size(min = 1, max = 255) String title,
        String content,
        @NotNull @Min(0) Integer version) {
}
