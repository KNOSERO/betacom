package com.ravcube.betacom.service;

import com.ravcube.betacom.api.dto.ItemListResponseDTO;
import com.ravcube.betacom.api.dto.ItemResponseDTO;
import com.ravcube.betacom.api.dto.UpdatedItemResponseDTO;
import com.ravcube.betacom.database.repository.ItemEntityRepository;
import com.ravcube.betacom.domain.ItemProcessService;
import com.ravcube.betacom.domain.SessionHelper;
import com.ravcube.betacom.domain.spec.ItemResponse;
import com.ravcube.betacom.domain.spec.UpdatedItemResponse;
import com.ravcube.betacom.service.mapper.ItemListMapper;
import com.ravcube.betacom.service.mapper.ItemMapper;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ItemService {

    private final ItemEntityRepository itemRepository;
    private final ItemListMapper itemListMapper;
    private final ItemProcessService itemProcessService;
    private final SessionHelper sessionHelper;
    private final ItemMapper itemMapper;

    public ItemService(
            ItemEntityRepository itemRepository,
            ItemListMapper itemListMapper,
            ItemProcessService itemProcessService,
            SessionHelper sessionHelper,
            ItemMapper itemMapper) {
        this.itemRepository = itemRepository;
        this.itemListMapper = itemListMapper;
        this.itemProcessService = itemProcessService;
        this.sessionHelper = sessionHelper;
        this.itemMapper = itemMapper;
    }

    public List<ItemListResponseDTO> findAll() {
        UUID mockUserId = sessionHelper.getUserId();

        return itemRepository.findVisibleItems(mockUserId)
                .stream()
                .map(itemListMapper::toResponse)
                .toList();
    }

    public ItemResponseDTO create(String title, String content) {
        ItemResponse itemResponse = itemProcessService.create(title, content);
        return itemMapper.toResponseDTO(itemResponse);
    }

    @Transactional
    public UpdatedItemResponseDTO update(UUID itemId, String title, String content, Long version) {
        UpdatedItemResponse updatedItemResponse = itemProcessService.update(itemId, title, content, version);
        return itemMapper.toUpdatedResponseDTO(updatedItemResponse);
    }

    @Transactional
    public void delete(UUID itemId) {
        itemProcessService.delete(itemId);
    }
}
