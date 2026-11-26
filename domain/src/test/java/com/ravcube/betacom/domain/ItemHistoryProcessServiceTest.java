package com.ravcube.betacom.domain;

import com.ravcube.betacom.domain.item.AccessItemException;
import com.ravcube.betacom.domain.port.ItemHistoryRepository;
import com.ravcube.betacom.domain.port.ItemRepository;
import com.ravcube.betacom.domain.port.SessionHelper;
import com.ravcube.betacom.domain.spec.*;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ItemHistoryProcessServiceTest {
    private final ItemRepository items = mock(ItemRepository.class);
    private final ItemHistoryRepository history = mock(ItemHistoryRepository.class);
    private final SessionHelper session = mock(SessionHelper.class);
    private final ItemHistoryProcessService service = new ItemHistoryProcessService(items, history, session);

    @Test
    void givenOwner_whenRequestingItemHistory_thenHistoryIsReturned() {
        // Given
        UUID ownerId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();
        List<ItemHistoryEntry> expected = List.of(new ItemHistoryEntry(1, "ADD", Instant.now(), ownerId, "Title", "Body"));
        when(session.getUserId()).thenReturn(ownerId);
        when(items.findById(itemId)).thenReturn(new ItemSpec(itemId, "Title", "Body", 0L, ownerId, List.of()));
        when(history.findHistory(itemId)).thenReturn(expected);

        // When
        List<ItemHistoryEntry> actual = service.findHistory(itemId);

        // Then
        assertEquals(expected, actual);
        verify(history).findHistory(itemId);
    }

    @Test
    void givenViewerPermission_whenRequestingItemHistory_thenHistoryIsReturned() {
        // Given
        UUID readerId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();
        List<ItemHistoryEntry> expected = List.of(
                new ItemHistoryEntry(1, "ADD", Instant.now(), readerId, "Title", "Body"));
        when(session.getUserId()).thenReturn(readerId);
        when(items.findById(itemId)).thenReturn(new ItemSpec(itemId, "Title", "Body", 0L, UUID.randomUUID(),
                List.of(new PermissionSpec(readerId, PermissionSpec.Role.VIEWER))));
        when(history.findHistory(itemId)).thenReturn(expected);

        // When
        List<ItemHistoryEntry> actual = service.findHistory(itemId);

        // Then
        assertEquals(expected, actual);
        verify(history).findHistory(itemId);
    }

    @Test
    void givenUserWithoutReadPermission_whenRequestingItemHistory_thenAccessIsDenied() {
        // Given
        UUID itemId = UUID.randomUUID();
        when(session.getUserId()).thenReturn(UUID.randomUUID());
        when(items.findById(itemId)).thenReturn(new ItemSpec(itemId, "Title", "Body", 0L, UUID.randomUUID(), List.of()));

        // When / Then
        assertThrows(AccessItemException.class, () -> service.findHistory(itemId));
        verifyNoInteractions(history);
    }
}
