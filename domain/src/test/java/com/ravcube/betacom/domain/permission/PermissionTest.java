package com.ravcube.betacom.domain.permission;

import com.ravcube.betacom.domain.spec.PermissionSpec;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PermissionTest {

    @Test
    void createMapsEditorRoleToSpec() {
        UUID userId = UUID.randomUUID();

        PermissionSpec spec = Permission.create(userId, "EDITOR").toSpec();

        assertEquals(userId, spec.userId());
        assertEquals(PermissionSpec.Role.EDITOR, spec.role());
    }

    @Test
    void fromSpecRestoresPermission() {
        UUID userId = UUID.randomUUID();

        Permission permission = Permission.fromSpec(new PermissionSpec(userId, PermissionSpec.Role.VIEWER));

        assertTrue(permission.hasPermission(userId));
        assertFalse(permission.hasEditor(userId));
        assertEquals(new PermissionSpec(userId, PermissionSpec.Role.VIEWER), permission.toSpec());
    }

    @Test
    void editorHasEditPermission() {
        UUID userId = UUID.randomUUID();
        Permission permission = Permission.create(userId, "EDITOR");

        assertTrue(permission.hasPermission(userId));
        assertTrue(permission.hasEditor(userId));
    }

    @Test
    void viewerDoesNotHaveEditPermission() {
        UUID userId = UUID.randomUUID();
        Permission permission = Permission.create(userId, "VIEWER");

        assertTrue(permission.hasPermission(userId));
        assertFalse(permission.hasEditor(userId));
    }

    @Test
    void anotherUserDoesNotHavePermission() {
        Permission permission = Permission.create(UUID.randomUUID(), "EDITOR");

        assertFalse(permission.hasPermission(UUID.randomUUID()));
        assertFalse(permission.hasEditor(UUID.randomUUID()));
    }

    @Test
    void createRejectsNullUserId() {
        assertThrows(InvalidPermissionException.class, () -> Permission.create(null, "VIEWER"));
    }

    @Test
    void createRejectsNullRole() {
        assertThrows(InvalidPermissionException.class, () -> Permission.create(UUID.randomUUID(), null));
    }

    @Test
    void createRejectsRoleThatCannotBeMappedToPermissionRole() {
        assertThrows(InvalidPermissionException.class, () -> Permission.create(UUID.randomUUID(), "OWNER"));
    }

    @Test
    void fromSpecRejectsNullSpec() {
        assertThrows(InvalidPermissionException.class, () -> Permission.fromSpec(null));
    }
}
