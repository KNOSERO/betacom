package com.ravcube.betacom.database.repository;

import com.ravcube.betacom.database.entity.ItemEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ItemRepository extends JpaRepository<ItemEntity, UUID>, JpaSpecificationExecutor<ItemEntity> {
}
