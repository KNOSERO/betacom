package com.ravcube.betacom.domain;

import com.ravcube.betacom.domain.port.ItemHistoryRepository;
import com.ravcube.betacom.domain.port.ItemRepository;
import com.ravcube.betacom.domain.port.SessionHelper;
import com.ravcube.betacom.domain.item.Item;
import com.ravcube.betacom.domain.spec.ItemHistoryEntry;
import java.util.List;
import java.util.UUID;

public final class ItemHistoryProcessService {

    private final ItemRepository itemRepository;
    private final ItemHistoryRepository itemHistoryRepository;
    private final SessionHelper sessionHelper;

    public ItemHistoryProcessService(
            ItemRepository itemRepository,
            ItemHistoryRepository itemHistoryRepository,
            SessionHelper sessionHelper) {
        this.itemRepository = itemRepository;
        this.itemHistoryRepository = itemHistoryRepository;
        this.sessionHelper = sessionHelper;
    }

    public List<ItemHistoryEntry> findHistory(UUID itemId) {
        UUID userId = sessionHelper.getUserId();
        Item item = Item.fromSpec(userId, itemRepository.findById(itemId));
        item.isReadableBy(userId);
        return itemHistoryRepository.findHistory(itemId);
    }
}
