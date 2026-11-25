package com.ravcube.betacom.database.repository;

import com.ravcube.betacom.database.entity.ItemPermissionEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemPermissionRepository extends JpaRepository<ItemPermissionEntity, UUID> {
}
