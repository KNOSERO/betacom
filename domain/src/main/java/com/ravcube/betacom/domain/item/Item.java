package com.ravcube.betacom.domain.item;

import com.ravcube.betacom.domain.note.InvalidNoteException;
import com.ravcube.betacom.domain.note.Note;
import com.ravcube.betacom.domain.permission.Permission;
import com.ravcube.betacom.domain.spec.ItemSpec;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public final class Item {

    private final UUID id;
    private final Long version;
    private Note note;
    private final UUID ownerId;
    private final List<Permission> permission;

    private Item(UUID id, Long version, Note note, UUID ownerId, List<Permission> permission) {
        this.id = Objects.requireNonNull(id, "id");
        this.version = Objects.requireNonNull(version, "version");
        this.note = Objects.requireNonNull(note, "note");
        this.ownerId = Objects.requireNonNull(ownerId, "isOwner");
        this.permission = Objects.requireNonNull(permission, "permission");
    }

    public static Item fromSpec(UUID userId, ItemSpec itemSpec) {
        return new Item(itemSpec.id(),
                itemSpec.version(),
                new Note(itemSpec.title(), itemSpec.content()),
                itemSpec.ownerId(),
                itemSpec.permissions()
                        .stream()
                        .map(Permission::fromSpec)
                        .toList()
        );
    }

    public ItemSpec toSpec() {
        return new ItemSpec(
                this.id,
                this.note.getTitle(),
                this.note.getContent(),
                this.version,
                this.ownerId,
                this.permission.stream()
                        .map(Permission::toSpec)
                        .toList()
        );
    }

    public static Item create(String title, String content, UUID ownerId) {
        final List<Permission> permissions = new ArrayList<>();
        return new Item(UUID.randomUUID(), 0L, new Note(title, content), ownerId, permissions);
    }

    public void update(Note note, Long expectedVersion) {
        if (!expectedVersion.equals(this.version)) {
            throw new ItemVersionConflictException(this.id, expectedVersion, this.version);
        }
        this.note = Objects.requireNonNull(note, "note");
    }

    public void update(
            String title,
            boolean titleProvided,
            String content,
            boolean contentProvided,
            Long expectedVersion) {
        String updatedTitle = titleProvided && title != null ? title : this.note.getTitle();
        String updatedContent = contentProvided ? content : this.note.getContent();
        update(new Note(updatedTitle, updatedContent), expectedVersion);
    }

    public void isEditor(UUID id) {
        if (this.ownerId.equals(id) || this.permission.stream().anyMatch(permission -> permission.haveEditor(id))) {
            return;
        }
        throw new AccessItemException("Have no permission to edit this item");
    }

    public void isOwner(UUID id) {
        if (this.ownerId.equals(id)) {
            return;
        }
        throw new AccessItemException("You no be owner to edit this item");
    }

}
