package com.ravcube.betacom.database.audit;

import java.util.UUID;

public final class AuditRevisionContext {

    private static final ThreadLocal<UUID> CREATED_BY = new ThreadLocal<>();

    private AuditRevisionContext() {
    }

    public static void setCreatedBy(UUID createdBy) {
        CREATED_BY.set(createdBy);
    }

    public static UUID getCreatedBy() {
        return CREATED_BY.get();
    }

    public static void clear() {
        CREATED_BY.remove();
    }
}
