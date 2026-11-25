package com.ravcube.betacom.database.specification;

import com.ravcube.betacom.database.entity.ItemPermissionEntity;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;

public final class ItemPermissionSpecifications {

    private ItemPermissionSpecifications() {
    }

    public static Specification<ItemPermissionEntity> forItem(UUID itemId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("item").get("id"), itemId);
    }

    public static Specification<ItemPermissionEntity> forItemAndUser(UUID itemId, UUID userId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.and(
                criteriaBuilder.equal(root.get("item").get("id"), itemId),
                criteriaBuilder.equal(root.get("user").get("id"), userId));
    }
}
