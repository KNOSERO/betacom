package com.ravcube.betacom.database.mapper;

import com.ravcube.betacom.database.entity.ItemEntity;
import com.ravcube.betacom.database.entity.ItemPermissionEntity;
import com.ravcube.betacom.database.entity.PermissionRole;
import com.ravcube.betacom.database.entity.UserEntity;
import com.ravcube.betacom.domain.spec.PermissionSpec;
import com.ravcube.betacom.domain.spec.ItemSpec;
import com.ravcube.betacom.domain.spec.ItemResponse;
import org.springframework.stereotype.Component;
import com.ravcube.betacom.domain.spec.UpdatedItemResponse;

@Component
public class ItemEntityMapper {

    public ItemEntity toEntity(ItemSpec itemSpec, UserEntity owner) {
        return ItemEntity.builder()
                .id(itemSpec.id())
                .owner(owner)
                .title(itemSpec.title())
                .content(itemSpec.content())
                .build();
    }

    public ItemPermissionEntity toPermissionEntity(
            ItemEntity item,
            UserEntity user,
            PermissionSpec permission) {
        return ItemPermissionEntity.builder()
                .item(item)
                .user(user)
                .role(PermissionRole.valueOf(permission.role().name()))
                .build();
    }

    public ItemSpec toSpec(ItemEntity entity) {
        return new ItemSpec(
                entity.getId(),
                entity.getTitle(),
                entity.getContent(),
                entity.getVersion().longValue(),
                entity.getOwner().getId(),
                entity.getPermissions().stream()
                        .map(this::toPermissionSpec)
                        .toList());
    }

    private PermissionSpec toPermissionSpec(ItemPermissionEntity permission) {
        return new PermissionSpec(
                permission.getUser().getId(),
                PermissionSpec.Role.valueOf(permission.getRole().name()));
    }

    public ItemResponse toResponse(ItemEntity entity) {
        return new ItemResponse(
                entity.getId(),
                entity.getTitle(),
                entity.getContent(),
                entity.getVersion(),
                entity.getOwner().getId(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }

    public UpdatedItemResponse toUpdatedResponse(ItemEntity entity) {
        return new UpdatedItemResponse(
                entity.getId(),
                entity.getTitle(),
                entity.getContent(),
                entity.getVersion(),
                entity.getUpdatedAt());
    }
}
