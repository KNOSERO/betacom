package com.ravcube.betacom.domain;

import com.ravcube.betacom.domain.port.ItemPermissionRepository;
import com.ravcube.betacom.domain.port.ItemRepository;
import com.ravcube.betacom.domain.port.SessionHelper;
import com.ravcube.betacom.domain.item.Item;
import com.ravcube.betacom.domain.permission.Permission;
import com.ravcube.betacom.domain.spec.ShareItemResponse;

import java.util.UUID;

public class ItemPermissionProcessService {

    private final ItemRepository itemRepository;
    private final ItemPermissionRepository itemPermissionRepository;
    private final SessionHelper sessionHelper;

    public ItemPermissionProcessService(ItemRepository itemRepository, ItemPermissionRepository itemPermissionRepository, SessionHelper sessionHelper) {
        this.itemRepository = itemRepository;
        this.itemPermissionRepository = itemPermissionRepository;
        this.sessionHelper = sessionHelper;
    }

    public ShareItemResponse share(UUID itemId, UUID userId, String role) {
        UUID currentUserId = sessionHelper.getUserId();
        Item item = Item.fromSpec(currentUserId, itemRepository.findById(itemId));
        item.isOwner(currentUserId);
        if(item.updatePermission(userId)) {
            return itemPermissionRepository.update(itemId, Permission.create(userId, role).toSpec());
        };
        return itemPermissionRepository.create(itemId, Permission.create(userId, role).toSpec());
    }

    public void delete(UUID itemId, UUID userId) {
        UUID currentUserId = sessionHelper.getUserId();
        Item item = Item.fromSpec(currentUserId, itemRepository.findById(itemId));
        item.isOwner(currentUserId);
        itemPermissionRepository.delete(itemId, userId);
    }
}
