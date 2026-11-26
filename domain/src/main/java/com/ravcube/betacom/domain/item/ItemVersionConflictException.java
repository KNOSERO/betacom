package com.ravcube.betacom.domain.item;

import java.util.UUID;

public final class ItemVersionConflictException extends RuntimeException {

    private final UUID itemId;
    private final long expectedVersion;
    private final long currentVersion;

    public ItemVersionConflictException(UUID itemId, long expectedVersion, long actualVersion) {
        super("Item %s has version %d; version %d was expected."
                .formatted(itemId, actualVersion, expectedVersion));
        this.itemId = itemId;
        this.expectedVersion = expectedVersion;
        this.currentVersion = actualVersion;
    }

    public UUID getItemId() {
        return itemId;
    }

    public long getExpectedVersion() {
        return expectedVersion;
    }

    public long getCurrentVersion() {
        return currentVersion;
    }
}
