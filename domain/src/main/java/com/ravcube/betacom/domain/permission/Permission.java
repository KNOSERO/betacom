package com.ravcube.betacom.domain.permission;

import com.ravcube.betacom.domain.spec.PermissionSpec;

import java.util.Objects;
import java.util.UUID;

public final class Permission {

    private final UUID userId;
    private final Role role;

    private Permission(UUID userId, Role role) {
        this.userId = Objects.requireNonNull(userId, "userId");
        this.role = Objects.requireNonNull(role, "role");
    }

    public static Permission fromSpec(PermissionSpec spec) {
        Objects.requireNonNull(spec, "spec");
        return new Permission(spec.userId(), Role.valueOf(spec.role().name()));
    }

    public PermissionSpec toSpec() {
        return new PermissionSpec(this.userId, PermissionSpec.Role.valueOf(this.role.name()));
    }

    public Boolean havePermission(UUID userId) {
        return this.userId.equals(userId);
    }

    public Boolean haveEditor(UUID userId) {
        return this.userId.equals(userId) && this.role == Role.EDITOR;
    }

    public enum Role {
        VIEWER,
        EDITOR
    }
}
