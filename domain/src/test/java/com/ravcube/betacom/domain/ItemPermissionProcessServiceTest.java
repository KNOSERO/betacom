package com.ravcube.betacom.domain;

import com.ravcube.betacom.domain.item.AccessItemException;
import com.ravcube.betacom.domain.permission.InvalidPermissionException;
import com.ravcube.betacom.domain.port.ItemPermissionRepository;
import com.ravcube.betacom.domain.port.ItemRepository;
import com.ravcube.betacom.domain.port.SessionHelper;
import com.ravcube.betacom.domain.spec.*;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ItemPermissionProcessServiceTest {
    private final ItemRepository items = mock(ItemRepository.class);
    private final ItemPermissionRepository permissions = mock(ItemPermissionRepository.class);
    private final SessionHelper session = mock(SessionHelper.class);
    private final ItemPermissionProcessService service = new ItemPermissionProcessService(items, permissions, session);

    @Test
    void givenOwnerAndUnsharedUser_whenSharingAsEditor_thenEditorPermissionIsCreated() {
        // Given
        UUID ownerId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();
        UUID recipientId = UUID.randomUUID();
        ShareItemResponse expected = new ShareItemResponse(itemId, recipientId, ShareRole.EDITOR, Instant.now(), true);
        when(session.getUserId()).thenReturn(ownerId);
        when(items.findById(itemId)).thenReturn(new ItemSpec(itemId, "Title", "Content", 0L, ownerId, List.of()));
        when(permissions.create(itemId, new PermissionSpec(recipientId, PermissionSpec.Role.EDITOR))).thenReturn(expected);

        // When
        ShareItemResponse actual = service.share(itemId, recipientId, "EDITOR");

        // Then
        assertEquals(expected, actual);
        verify(permissions).create(itemId, new PermissionSpec(recipientId, PermissionSpec.Role.EDITOR));
        verify(permissions, never()).update(any(), any());
    }

    @Test
    void givenExistingViewerPermission_whenOwnerSharesAsEditor_thenPermissionIsUpdated() {
        // Given
        UUID ownerId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();
        UUID recipientId = UUID.randomUUID();
        ShareItemResponse expected = new ShareItemResponse(itemId, recipientId, ShareRole.EDITOR, Instant.now(), false);
        when(session.getUserId()).thenReturn(ownerId);
        when(items.findById(itemId)).thenReturn(new ItemSpec(itemId, "Title", "Content", 0L, ownerId,
                List.of(new PermissionSpec(recipientId, PermissionSpec.Role.VIEWER))));
        when(permissions.update(itemId, new PermissionSpec(recipientId, PermissionSpec.Role.EDITOR))).thenReturn(expected);

        // When
        ShareItemResponse actual = service.share(itemId, recipientId, "EDITOR");

        // Then
        assertEquals(expected, actual);
        verify(permissions).update(itemId, new PermissionSpec(recipientId, PermissionSpec.Role.EDITOR));
        verify(permissions, never()).create(any(), any());
    }

    @Test
    void givenUnsupportedRole_whenOwnerSharesItem_thenRequestIsRejected() {
        // Given
        UUID ownerId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();
        when(session.getUserId()).thenReturn(ownerId);
        when(items.findById(itemId)).thenReturn(new ItemSpec(itemId, "Title", "Content", 0L, ownerId, List.of()));

        // When / Then
        assertThrows(InvalidPermissionException.class, () -> service.share(itemId, UUID.randomUUID(), "ADMIN"));
        verifyNoInteractions(permissions);
    }

    @Test
    void givenOwner_whenSharingItemWithSelf_thenRequestIsRejectedAndPermissionIsNotPersisted() {
        // Given
        UUID ownerId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();
        when(session.getUserId()).thenReturn(ownerId);
        when(items.findById(itemId)).thenReturn(new ItemSpec(
                itemId, "Title", "Content", 0L, ownerId, List.of()));

        // When / Then
        assertThrows(InvalidPermissionException.class,
                () -> service.share(itemId, ownerId, "VIEWER"));
        verifyNoInteractions(permissions);
    }

    @Test
    void givenNonOwner_whenSharingItem_thenAccessIsDeniedAndPermissionIsNotCreated() {
        // Given
        UUID itemId = UUID.randomUUID();
        when(session.getUserId()).thenReturn(UUID.randomUUID());
        when(items.findById(itemId)).thenReturn(new ItemSpec(itemId, "Title", "Content", 0L, UUID.randomUUID(), List.of()));

        // When / Then
        assertThrows(AccessItemException.class, () -> service.share(itemId, UUID.randomUUID(), "VIEWER"));
        verifyNoInteractions(permissions);
    }

    @Test
    void givenOwner_whenRevokingPermission_thenPermissionIsDeletedForRecipient() {
        // Given
        UUID ownerId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();
        UUID recipientId = UUID.randomUUID();
        when(session.getUserId()).thenReturn(ownerId);
        when(items.findById(itemId)).thenReturn(new ItemSpec(itemId, "Title", "Content", 0L, ownerId, List.of()));

        // When
        service.delete(itemId, recipientId);

        // Then
        verify(permissions).delete(itemId, recipientId);
    }

    @Test
    void givenNonOwner_whenRevokingPermission_thenAccessIsDeniedAndPermissionIsNotDeleted() {
        // Given
        UUID itemId = UUID.randomUUID();
        when(session.getUserId()).thenReturn(UUID.randomUUID());
        when(items.findById(itemId)).thenReturn(new ItemSpec(itemId, "Title", "Content", 0L, UUID.randomUUID(), List.of()));

        // When / Then
        assertThrows(AccessItemException.class, () -> service.delete(itemId, UUID.randomUUID()));
        verifyNoInteractions(permissions);
    }
}
