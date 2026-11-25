package com.ravcube.betacom.domain.permission;

import com.ravcube.betacom.domain.spec.PermissionSpec;

import java.util.UUID;

public final class Permission {

    private final UUID userId;
    private Role role;

    private Permission(UUID userId, Role role) {
        this.userId = requireValue(userId, "userId");
        this.role = requireValue(role, "role");
    }

    private static <T> T requireValue(T value, String fieldName) {
        if (value == null) {
            throw new InvalidPermissionException(fieldName + " is required.");
        }
        return value;
    }

    public static Permission create(UUID userId, String role) {
        return new Permission(userId, parseRole(role));
    }

    private static Role parseRole(String role) {
        if (role == null) {
            throw new InvalidPermissionException("role is required.");
        }

        try {
            return Role.valueOf(role);
        } catch (IllegalArgumentException exception) {
            throw new InvalidPermissionException("Unsupported permission role: " + role);
        }
    }

    public static Permission fromSpec(PermissionSpec spec) {
        requireValue(spec, "permission spec");
        requireValue(spec.role(), "role");
        return new Permission(spec.userId(), Role.valueOf(spec.role().name()));
    }

    public PermissionSpec toSpec() {
        return new PermissionSpec(this.userId, PermissionSpec.Role.valueOf(this.role.name()));
    }

    public Boolean hasPermission(UUID userId) {
        return this.userId.equals(userId);
    }

    public Boolean hasEditor(UUID userId) {
        return this.userId.equals(userId) && this.role == Role.EDITOR;
    }

    public enum Role {
        VIEWER,
        EDITOR
    }
}
