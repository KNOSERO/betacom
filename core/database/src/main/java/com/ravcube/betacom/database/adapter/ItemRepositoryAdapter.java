package com.ravcube.betacom.database.adapter;

import com.ravcube.betacom.database.entity.ItemEntity;
import com.ravcube.betacom.database.entity.UserEntity;
import com.ravcube.betacom.database.mapper.ItemEntityMapper;
import com.ravcube.betacom.database.repository.ItemEntityRepository;
import com.ravcube.betacom.database.repository.UserEntityRepository;
import com.ravcube.betacom.domain.ItemRepository;
import com.ravcube.betacom.domain.spec.ItemResponse;
import com.ravcube.betacom.domain.spec.ItemSpec;

import java.util.UUID;

import com.ravcube.betacom.domain.spec.UpdatedItemResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Repository;

@Repository
public class ItemRepositoryAdapter implements ItemRepository {

    private final ItemEntityRepository repository;
    private final UserEntityRepository userRepository;
    private final ItemEntityMapper itemEntityMapper;

    public ItemRepositoryAdapter(
            ItemEntityRepository repository,
            UserEntityRepository userRepository,
            ItemEntityMapper itemEntityMapper) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.itemEntityMapper = itemEntityMapper;
    }

    @Override
    public ItemSpec findById(UUID itemId) {
        ItemEntity item = repository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Item not found: " + itemId));
        return itemEntityMapper.toSpec(item);
    }

    @Override
    public ItemResponse create(ItemSpec itemSpec) {
        UserEntity owner = userRepository.getReferenceById(itemSpec.ownerId());
        ItemEntity entity = itemEntityMapper.toEntity(itemSpec, owner);
        ItemEntity saved = repository.saveAndFlush(entity);
        return itemEntityMapper.toResponse(saved);
    }

    @Override
    public UpdatedItemResponse update(ItemSpec itemSpec) {
        ItemEntity item = repository.findById(itemSpec.id())
                .orElseThrow(() -> new EntityNotFoundException("Item not found: " + itemSpec.id()));
        item.setTitle(itemSpec.title());
        item.setContent(itemSpec.content());
        ItemEntity updated = repository.saveAndFlush(item);
        return itemEntityMapper.toUpdatedResponse(updated);
    }

    @Override
    public void delete(ItemSpec itemSpec) {
        repository.deleteById(itemSpec.id());
    }
}
