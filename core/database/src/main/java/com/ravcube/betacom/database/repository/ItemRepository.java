package com.ravcube.betacom.database.repository;

import com.ravcube.betacom.database.entity.ItemEntity;

import java.util.List;
import java.util.UUID;

import com.ravcube.betacom.database.projection.ItemListProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ItemRepository extends JpaRepository<ItemEntity, UUID> {

    @Query("""
            select i.id as id,
                   i.title as title,
                   i.content as content,
                   i.version as version,
                   i.owner.id as ownerId,
                   case
                       when i.owner.id = :userId then 'OWNER'
                       when exists (
                           select editorPermission.id
                           from ItemPermissionEntity editorPermission
                           where editorPermission.item.id = i.id
                             and editorPermission.user.id = :userId
                             and editorPermission.role =
                                 com.ravcube.betacom.database.entity.PermissionRole.EDITOR
                       ) then 'EDITOR'
                       else 'VIEWER'
                   end as myRole,
                   i.updatedAt as updatedAt
            from ItemEntity i
            where i.deleted = false
              and (
                  i.owner.id = :userId
                  or exists (
                      select permission.id
                      from ItemPermissionEntity permission
                      where permission.item.id = i.id
                        and permission.user.id = :userId
                        and permission.role in (
                            com.ravcube.betacom.database.entity.PermissionRole.EDITOR,
                            com.ravcube.betacom.database.entity.PermissionRole.VIEWER)
                  )
              )
            """)
    List<ItemListProjection> findVisibleItems(@Param("userId") UUID userId);
}
