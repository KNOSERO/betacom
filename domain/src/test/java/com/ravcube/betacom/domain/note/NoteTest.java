package com.ravcube.betacom.domain.note;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class NoteTest {

    @Test
    void givenValidTitleAndNullContent_whenCreatingNote_thenCreatesNote() {
        // Given
        String title = "Shopping list";
        String content = null;

        // When
        Note note = new Note(title, content);

        // Then
        assertEquals(title, note.getTitle());
        assertNull(note.getContent());
    }

    @Test
    void givenOneCharacterTitle_whenCreatingNote_thenCreatesNote() {
        // Given
        String title = "N";

        // When
        Note note = new Note(title, "content");

        // Then
        assertEquals(title, note.getTitle());
    }

    @Test
    void given255CharacterTitle_whenCreatingNote_thenCreatesNote() {
        // Given
        String title = "a".repeat(255);

        // When
        Note note = new Note(title, "content");

        // Then
        assertEquals(title, note.getTitle());
    }

    @Test
    void givenNullTitle_whenCreatingNote_thenThrowsInvalidNoteException() {
        // Given
        String title = null;

        // When / Then
        assertThrows(InvalidNoteException.class, () -> new Note(title, null));
    }

    @Test
    void givenBlankTitle_whenCreatingNote_thenThrowsInvalidNoteException() {
        // Given
        String title = "   ";

        // When / Then
        assertThrows(InvalidNoteException.class, () -> new Note(title, null));
    }

    @Test
    void given256CharacterTitle_whenCreatingNote_thenThrowsInvalidNoteException() {
        // Given
        String title = "a".repeat(256);

        // When / Then
        assertThrows(InvalidNoteException.class, () -> new Note(title, null));
    }
}
