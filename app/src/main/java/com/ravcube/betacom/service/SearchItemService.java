package com.ravcube.betacom.service;

import com.ravcube.betacom.api.dto.ItemListResponse;
import com.ravcube.betacom.database.repository.ItemRepository;
import com.ravcube.betacom.service.mapper.ItemListMapper;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class SearchItemService {

    private final ItemRepository itemRepository;
    private final ItemListMapper itemListMapper;

    public SearchItemService(ItemRepository itemRepository, ItemListMapper itemListMapper) {
        this.itemRepository = itemRepository;
        this.itemListMapper = itemListMapper;
    }

    public List<ItemListResponse> findAll() {
        //TODO: czeka na modół logowania
        UUID mockUserId = UUID.randomUUID();

        return itemRepository.findVisibleItems(mockUserId)
                .stream()
                .map(itemListMapper::toResponse)
                .toList();
    }
}
