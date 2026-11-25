package com.ravcube.betacom.api.dto;

public record LoginResponse(String token, long expiresIn) {
}
