package com.ravcube.betacomp.api.dto;

public record LoginResponse(String token, long expiresIn) {
}
