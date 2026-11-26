package com.ravcube.betacom.domain.spec;

import java.util.UUID;

public record LoginCredential(UUID userId, String login, String encodedPassword) {
}
