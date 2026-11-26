package com.ravcube.betacom.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequestDTO(
        @NotBlank @Size(min = 3, max = 64) String login,
        @NotBlank @Size(min = 8) String password) {
}
