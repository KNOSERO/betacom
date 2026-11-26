package com.ravcube.betacom.database.projection;

import java.time.Instant;
import java.util.UUID;

public interface ItemListProjection {

    UUID getId();

    String getTitle();

    String getContent();

    Integer getVersion();

    UUID getOwnerId();

    String getMyRole();

    Instant getUpdatedAt();
}
