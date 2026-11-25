package com.ravcube.betacom.domain.item;

import java.util.UUID;

public final class ItemVersionConflictException extends RuntimeException {

    public ItemVersionConflictException(UUID itemId, long expectedVersion, long actualVersion) {
        super("Item %s has version %d; version %d was expected."
                .formatted(itemId, actualVersion, expectedVersion));
    }
}
