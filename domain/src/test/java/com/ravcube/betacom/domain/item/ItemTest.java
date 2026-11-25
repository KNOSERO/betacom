package com.ravcube.betacom.domain.item;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ravcube.betacom.domain.note.InvalidNoteException;
import com.ravcube.betacom.domain.spec.ItemSpec;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ItemTest {

    @Test
    void givenNewItem_whenUpdatingNote_thenStoresNewTitleAndContent() {
        // Given
        Item item = Item.create("Old title", "Old content", UUID.randomUUID());

        // When
        item.update("New title", "New content", 0L);

        // Then
        assertEquals("New title", item.toSpec().title());
        assertEquals("New content", item.toSpec().content());
    }

    @Test
    void givenItemFromSpec_whenUpdatingNote_thenKeepsIdentityAndStoresChanges() {
        // Given
        UUID itemId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();
        ItemSpec storedItem = new ItemSpec(itemId, "Stored title", "Stored content", 4L, ownerId, List.of());
        Item item = Item.fromSpec(ownerId, storedItem);

        // When
        item.update("Updated title", "Updated content", 4L);

        // Then
        ItemSpec result = item.toSpec();
        assertEquals(itemId, result.id());
        assertEquals(ownerId, result.ownerId());
        assertEquals(4L, result.version());
        assertEquals("Updated title", result.title());
        assertEquals("Updated content", result.content());
    }

    @Test
    void givenNullUpdateFields_whenUpdating_thenKeepsCurrentNote() {
        // Given
        Item item = Item.create("Existing title", "Existing content", UUID.randomUUID());

        // When
        item.update(null, null, 0L);

        // Then
        assertEquals("Existing title", item.toSpec().title());
        assertEquals("Existing content", item.toSpec().content());
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
    void givenBlankTitle_whenCreating_thenThrowsInvalidNoteException() {
        assertThrows(InvalidNoteException.class,
                () -> Item.create("   ", "Content", UUID.randomUUID()));
    }

    @Test
    void givenTitleOver255Characters_whenCreating_thenThrowsInvalidNoteException() {
        assertThrows(InvalidNoteException.class,
                () -> Item.create("a".repeat(256), "Content", UUID.randomUUID()));
    }

    @Test
    void givenBoundaryTitleAndNullContent_whenCreating_thenCreatesItem() {
        // Given / When
        Item shortestTitle = Item.create("a", null, UUID.randomUUID());
        Item longestTitle = Item.create("a".repeat(255), null, UUID.randomUUID());

        // Then
        assertEquals("a", shortestTitle.toSpec().title());
        assertEquals(255, longestTitle.toSpec().title().length());
        assertNull(shortestTitle.toSpec().content());
        assertNull(longestTitle.toSpec().content());
    }

    @Test
    void givenNullIdInSpec_whenRestoringItem_thenThrowsInvalidItemException() {
        // Given
        ItemSpec invalidSpec = new ItemSpec(null, "Title", "Content", 0L, UUID.randomUUID(), List.of());

        // When / Then
        assertThrows(InvalidItemException.class, () -> Item.fromSpec(UUID.randomUUID(), invalidSpec));
    }

    @Test
    void givenNullOwnerId_whenCreating_thenThrowsInvalidItemException() {
        assertThrows(InvalidItemException.class, () -> Item.create("Title", "Content", null));
    }
}
