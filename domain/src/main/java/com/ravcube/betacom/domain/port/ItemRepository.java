package com.ravcube.betacom.domain.port;

import com.ravcube.betacom.domain.spec.ItemResponse;
import com.ravcube.betacom.domain.spec.ItemSpec;
import com.ravcube.betacom.domain.spec.UpdatedItemResponse;

import java.util.UUID;

public interface ItemRepository {

    ItemSpec findById(UUID itemId);
    ItemResponse create(ItemSpec itemSpec);
    UpdatedItemResponse update(ItemSpec itemSpec);
    void delete(ItemSpec itemSpec);

}
