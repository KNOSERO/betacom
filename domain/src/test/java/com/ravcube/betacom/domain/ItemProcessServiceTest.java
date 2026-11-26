package com.ravcube.betacom.domain;

import com.ravcube.betacom.domain.item.AccessItemException;
import com.ravcube.betacom.domain.port.ItemRepository;
import com.ravcube.betacom.domain.port.SessionHelper;
import com.ravcube.betacom.domain.spec.*;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ItemProcessServiceTest {
    private final ItemRepository items = mock(ItemRepository.class);
    private final SessionHelper session = mock(SessionHelper.class);
    private final ItemProcessService service = new ItemProcessService(items, session);

    @Test
    void givenAuthenticatedUser_whenCreatingItem_thenItemIsOwnedByThatUser() {
        // Given
        UUID userId = UUID.randomUUID();
        ItemResponse expected = new ItemResponse(UUID.randomUUID(), "Title", "Body", 0L, userId, Instant.now(), Instant.now());
        when(session.getUserId()).thenReturn(userId);
        when(items.create(any())).thenReturn(expected);

        // When
        ItemResponse actual = service.create("Title", "Body");

        // Then
        assertEquals(expected, actual);
        ArgumentCaptor<ItemSpec> captor = ArgumentCaptor.forClass(ItemSpec.class);
        verify(items).create(captor.capture());
        assertEquals(userId, captor.getValue().ownerId());
        assertEquals("Title", captor.getValue().title());
        assertEquals("Body", captor.getValue().content());
    }

    @Test
    void givenEditor_whenUpdatingTitle_thenTitleChangesAndContentIsPreserved() {
        // Given
        UUID editorId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();
        when(session.getUserId()).thenReturn(editorId);
        when(items.findById(itemId)).thenReturn(new ItemSpec(itemId, "Old", "Content", 0L,
                UUID.randomUUID(), List.of(new PermissionSpec(editorId, PermissionSpec.Role.EDITOR))));
        UpdatedItemResponse expected = new UpdatedItemResponse(itemId, "New", "Content", 0L, Instant.now());
        when(items.update(any())).thenReturn(expected);

        // When
        UpdatedItemResponse actual = service.update(itemId, "New", null, 0L);

        // Then
        assertEquals(expected, actual);
        ArgumentCaptor<ItemSpec> captor = ArgumentCaptor.forClass(ItemSpec.class);
        verify(items).update(captor.capture());
        assertEquals("New", captor.getValue().title());
        assertEquals("Content", captor.getValue().content());
    }

    @Test
    void givenViewer_whenUpdatingItem_thenAccessIsDeniedAndNothingIsPersisted() {
        // Given
        UUID viewerId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();
        when(session.getUserId()).thenReturn(viewerId);
        when(items.findById(itemId)).thenReturn(new ItemSpec(itemId, "Title", "Content", 0L,
                UUID.randomUUID(), List.of(new PermissionSpec(viewerId, PermissionSpec.Role.VIEWER))));

        // When / Then
        assertThrows(AccessItemException.class, () -> service.update(itemId, "New", "Body", 0L));
        verify(items, never()).update(any());
    }

    @Test
    void givenOwner_whenDeletingItem_thenItemIsDeleted() {
        // Given
        UUID ownerId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();
        when(session.getUserId()).thenReturn(ownerId);
        when(items.findById(itemId)).thenReturn(new ItemSpec(itemId, "Title", "Content", 0L, ownerId, List.of()));

        // When
        service.delete(itemId);

        // Then
        ArgumentCaptor<ItemSpec> captor = ArgumentCaptor.forClass(ItemSpec.class);
        verify(items).delete(captor.capture());
        assertEquals(itemId, captor.getValue().id());
    }

    @Test
    void givenNonOwner_whenDeletingItem_thenAccessIsDeniedAndNothingIsDeleted() {
        // Given
        UUID itemId = UUID.randomUUID();
        when(session.getUserId()).thenReturn(UUID.randomUUID());
        when(items.findById(itemId)).thenReturn(new ItemSpec(itemId, "Title", "Content", 0L, UUID.randomUUID(), List.of()));

        // When / Then
        assertThrows(AccessItemException.class, () -> service.delete(itemId));
        verify(items, never()).delete(any());
    }
}
