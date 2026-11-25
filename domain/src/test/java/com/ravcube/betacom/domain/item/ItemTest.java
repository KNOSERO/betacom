package com.ravcube.betacom.domain.item;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ravcube.betacom.domain.note.InvalidNoteException;
import com.ravcube.betacom.domain.note.Note;
import com.ravcube.betacom.domain.spec.ItemSpec;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ItemTest {

    @Test
    void givenExistingItem_whenUpdatingTitleAndContent_thenReplacesNote() {
        // Given
        Item item = Item.create("Old title", "Old content", UUID.randomUUID());

        // When
        item.update("New title", "New content", 0L);

        // Then
        ItemSpec result = item.toSpec();
        assertEquals("New title", result.title());
        assertEquals("New content", result.content());
    }

    @Test
    void givenNullOrBlankFields_whenUpdating_thenKeepsExistingValues() {
        // Given
        Item item = Item.create("Existing title", "Existing content", UUID.randomUUID());

        // When
        item.update(null, "   ", 0L);

        // Then
        ItemSpec result = item.toSpec();
        assertEquals("Existing title", result.title());
        assertEquals("   ", result.content());
    }

    @Test
    void givenStaleVersion_whenUpdating_thenThrowsVersionConflict() {
        // Given
        Item item = Item.create("Existing title", "Existing content", UUID.randomUUID());

        // When / Then
        assertThrows(ItemVersionConflictException.class,
                () -> item.update("New title", "New content", 1L));
    }

    @Test
    void givenNullId_whenConstructing_thenThrowsInvalidItemException() {
        assertThrows(InvalidItemException.class,
                () -> new Item(null, 0L, new Note("Title", null), UUID.randomUUID(), List.of()));
    }

    @Test
    void givenNullVersion_whenConstructing_thenThrowsInvalidItemException() {
        assertThrows(InvalidItemException.class,
                () -> new Item(UUID.randomUUID(), null, new Note("Title", null), UUID.randomUUID(), List.of()));
    }

    @Test
    void givenNullNote_whenConstructing_thenThrowsInvalidItemException() {
        assertThrows(InvalidItemException.class,
                () -> new Item(UUID.randomUUID(), 0L, null, UUID.randomUUID(), List.of()));
    }

    @Test
    void givenNullOwnerId_whenConstructing_thenThrowsInvalidItemException() {
        assertThrows(InvalidItemException.class,
                () -> new Item(UUID.randomUUID(), 0L, new Note("Title", null), null, List.of()));
    }

    @Test
    void givenNullPermissions_whenConstructing_thenThrowsInvalidItemException() {
        assertThrows(InvalidItemException.class,
                () -> new Item(UUID.randomUUID(), 0L, new Note("Title", null), UUID.randomUUID(), null));
    }
}
