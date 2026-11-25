package com.ravcube.betacom.domain;

import com.ravcube.betacom.domain.spec.PermissionSpec;
import com.ravcube.betacom.domain.spec.ShareItemResponse;

import java.util.UUID;

public interface ItemPermissionRepository {

    ShareItemResponse create(UUID itemId, PermissionSpec permission);
    ShareItemResponse update(UUID itemId, PermissionSpec permission);
    void delete(UUID itemId, UUID userId);

}
