package com.ravcube.betacom.domain.spec;

import java.util.UUID;

public record AuthenticatedUser(UUID userId, String login) {
}
