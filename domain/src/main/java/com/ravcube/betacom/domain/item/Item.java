package com.ravcube.betacom.domain.item;

import com.ravcube.betacom.domain.note.Note;
import com.ravcube.betacom.domain.permission.Permission;
import com.ravcube.betacom.domain.spec.ItemSpec;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class Item {

    private final UUID id;
    private final Long version;
    private Note note;
    private final UUID ownerId;
    private final List<Permission> permission;

    Item(UUID id, Long version, Note note, UUID ownerId, List<Permission> permission) {
        this.id = requireValue(id, "id");
        this.version = requireValue(version, "version");
        this.note = requireValue(note, "note");
        this.ownerId = requireValue(ownerId, "ownerId");
        this.permission = requireValue(permission, "permission");
    }

    private static <T> T requireValue(T value, String fieldName) {
        if (value == null) {
            throw new InvalidItemException(fieldName + " is required.");
        }
        return value;
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

    public void update(
            String title,
            String content,
            Long expectedVersion) {
        if(!this.version.equals(expectedVersion)) {
            throw new ItemVersionConflictException(this.id, expectedVersion, this.version);
        }

        String updatedTitle = title == null ? this.note.getTitle() : title;
        String updatedContent = content == null ? this.note.getContent() : content;
        this.note = new Note(updatedTitle, updatedContent);
    }

    public Boolean updatePermission(UUID userId) {
        if(ownerId.equals(userId)) {
            throw new RuntimeException("");
        }
        return this.permission.stream().anyMatch(el -> el.hasPermission(userId));
    }

    public void isEditor(UUID id) {
        if (this.ownerId.equals(id) || this.permission.stream().anyMatch(permission -> permission.hasEditor(id))) {
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
