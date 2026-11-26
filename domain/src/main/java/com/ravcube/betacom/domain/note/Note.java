package com.ravcube.betacom.domain.note;

public final class Note {

    private final String title;
    private final String content;

    public Note(String title, String content) {
        if (title == null || title.isBlank() || title.length() > 255) {
            throw new InvalidNoteException("Title must contain 1 to 255 characters.");
        }

        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
