package com.ravcube.betacom.domain.spec;

import java.util.UUID;

public record PermissionSpec(UUID userId, Role role) {

    public enum Role {
        VIEWER,
        EDITOR
    }
}
