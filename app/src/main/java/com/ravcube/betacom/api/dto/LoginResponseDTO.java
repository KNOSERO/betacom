package com.ravcube.betacom.api.dto;

public record LoginResponseDTO(String token, long expiresIn) {
}
