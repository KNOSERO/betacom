package com.ravcube.betacom.domain;

import com.ravcube.betacom.domain.item.Item;
import com.ravcube.betacom.domain.spec.ItemResponse;
import com.ravcube.betacom.domain.spec.UpdatedItemResponse;

import java.util.UUID;

public final class ItemProcessService {

    private final ItemRepository itemRepository;
    private final SessionHelper sessionHelper;

    public ItemProcessService(ItemRepository itemRepository, SessionHelper sessionHelper) {
        this.itemRepository = itemRepository;
        this.sessionHelper = sessionHelper;
    }

    public ItemResponse create(String title, String content) {
        Item item = Item.create(title, content, sessionHelper.getUserId());
        return itemRepository.create(item.toSpec());
    }

    public UpdatedItemResponse update(UUID itemId, String title, String content, Long version) {
        UUID userId = sessionHelper.getUserId();
        Item item = Item.fromSpec(userId, itemRepository.findById(itemId));
        item.isEditor(userId);
        item.update(title, content, version);
        return itemRepository.update(item.toSpec());
    }

    public void delete(UUID itemId) {
        UUID userId = sessionHelper.getUserId();
        Item item = Item.fromSpec(userId, itemRepository.findById(itemId));
        item.isOwner(userId);
        itemRepository.delete(item.toSpec());
    }
}
