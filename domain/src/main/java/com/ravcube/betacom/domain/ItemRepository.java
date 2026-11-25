package com.ravcube.betacom.domain;

import com.ravcube.betacom.domain.spec.ItemResponse;
import com.ravcube.betacom.domain.spec.ItemSpec;
import com.ravcube.betacom.domain.spec.UpdatedItemResponse;

import java.util.UUID;

/** Outgoing port used by the Item domain to persist a newly created item. */
public interface ItemRepository {

    ItemSpec findById(UUID itemId);
    ItemResponse create(ItemSpec itemSpec);
    UpdatedItemResponse update(ItemSpec itemSpec);
    void delete(ItemSpec itemSpec);

}
