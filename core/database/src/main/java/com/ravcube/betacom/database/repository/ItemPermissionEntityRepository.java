package com.ravcube.betacom.database.repository;

import com.ravcube.betacom.database.entity.ItemPermissionEntity;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemPermissionEntityRepository extends
        JpaRepository<ItemPermissionEntity, UUID>,
        JpaSpecificationExecutor<ItemPermissionEntity> {

}
