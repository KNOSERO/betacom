package com.ravcube.betacom.domain.spec;

import java.util.List;
import java.util.UUID;

/** Data exchanged between the Item domain and its persistence adapter. */
public record ItemSpec(
        UUID id,
        String title,
        String content,
        Long version,
        UUID ownerId,
        List<PermissionSpec> permissions) {
}
