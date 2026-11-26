package com.ravcube.betacom.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateItemRequestDTO(
        @NotBlank @Size(max = 255) String title,
        String content) {
}
