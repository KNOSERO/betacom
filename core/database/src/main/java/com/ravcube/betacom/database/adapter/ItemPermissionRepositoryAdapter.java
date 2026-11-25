package com.ravcube.betacom.database.adapter;

import com.ravcube.betacom.database.entity.ItemEntity;
import com.ravcube.betacom.database.entity.ItemPermissionEntity;
import com.ravcube.betacom.database.entity.PermissionRole;
import com.ravcube.betacom.database.entity.UserEntity;
import com.ravcube.betacom.database.mapper.ItemEntityMapper;
import com.ravcube.betacom.database.repository.ItemEntityRepository;
import com.ravcube.betacom.database.repository.ItemPermissionEntityRepository;
import com.ravcube.betacom.database.repository.UserEntityRepository;
import com.ravcube.betacom.database.specification.ItemPermissionSpecifications;
import com.ravcube.betacom.domain.ItemPermissionRepository;
import com.ravcube.betacom.domain.spec.PermissionSpec;
import com.ravcube.betacom.domain.spec.ShareItemResponse;
import com.ravcube.betacom.domain.spec.ShareRole;
import jakarta.persistence.EntityNotFoundException;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class ItemPermissionRepositoryAdapter implements ItemPermissionRepository {

    private final ItemPermissionEntityRepository permissionRepository;
    private final ItemEntityRepository itemRepository;
    private final UserEntityRepository userRepository;
    private final ItemEntityMapper itemEntityMapper;

    public ItemPermissionRepositoryAdapter(
            ItemPermissionEntityRepository permissionRepository,
            ItemEntityRepository itemRepository,
            UserEntityRepository userRepository,
            ItemEntityMapper itemEntityMapper) {
        this.permissionRepository = permissionRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.itemEntityMapper = itemEntityMapper;
    }

    @Override
    public ShareItemResponse create(UUID itemId, PermissionSpec permission) {
        ItemEntity item = itemRepository.getReferenceById(itemId);
        UserEntity user = userRepository.findById(permission.userId())
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + permission.userId()));

        ItemPermissionEntity entity = itemEntityMapper.toPermissionEntity(item, user, permission);

        ItemPermissionEntity saved = permissionRepository.saveAndFlush(entity);
        return toResponse(saved, true);
    }

    @Override
    public ShareItemResponse update(UUID itemId, PermissionSpec permission) {
        ItemPermissionEntity entity = findPermission(itemId, permission.userId());
        entity.setRole(PermissionRole.valueOf(permission.role().name()));
        ItemPermissionEntity updated = permissionRepository.saveAndFlush(entity);
        return toResponse(updated, false);
    }

    @Override
    public void delete(UUID itemId, UUID userId) {
        permissionRepository.delete(findPermission(itemId, userId));
    }

    private ItemPermissionEntity findPermission(UUID itemId, UUID userId) {
        return permissionRepository.findOne(ItemPermissionSpecifications.forItemAndUser(itemId, userId))
                .orElseThrow(() -> new EntityNotFoundException(
                        "Permission not found for item %s and user %s.".formatted(itemId, userId)));
    }

    private ShareItemResponse toResponse(ItemPermissionEntity entity, boolean created) {
        return new ShareItemResponse(
                entity.getItem().getId(),
                entity.getUser().getId(),
                ShareRole.valueOf(entity.getRole().name()),
                entity.getGrantedAt(),
                created);
    }
}
