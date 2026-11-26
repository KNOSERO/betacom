package com.ravcube.betacom.database.adapter;

import com.ravcube.betacom.database.audit.ItemRevisionEntity;
import com.ravcube.betacom.database.entity.ItemEntity;
import com.ravcube.betacom.domain.port.ItemHistoryRepository;
import com.ravcube.betacom.domain.spec.ItemHistoryEntry;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ItemHistoryRepositoryAdapter implements ItemHistoryRepository {

    private final EntityManager entityManager;

    public ItemHistoryRepositoryAdapter(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public List<ItemHistoryEntry> findHistory(UUID itemId) {
        AuditReader auditReader = AuditReaderFactory.get(entityManager);
        List<Object[]> revisions = auditReader.createQuery()
                .forRevisionsOfEntity(ItemEntity.class, false, true)
                .add(AuditEntity.id().eq(itemId))
                .addOrder(AuditEntity.revisionNumber().asc())
                .getResultList();

        return revisions.stream()
                .map(this::toHistoryEntry)
                .toList();
    }

    private ItemHistoryEntry toHistoryEntry(Object[] revision) {
        ItemEntity item = (ItemEntity) revision[0];
        ItemRevisionEntity revisionEntity = (ItemRevisionEntity) revision[1];
        RevisionType revisionType = (RevisionType) revision[2];

        return new ItemHistoryEntry(
                revisionEntity.getId(),
                revisionType.name(),
                Instant.ofEpochMilli(revisionEntity.getTimestamp()),
                revisionEntity.getCreatedBy(),
                item.getTitle(),
                item.getContent());
    }
}
