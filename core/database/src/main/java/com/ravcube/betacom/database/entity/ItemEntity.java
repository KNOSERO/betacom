package com.ravcube.betacom.database.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "items")
@SQLDelete(sql = "UPDATE items SET deleted = true, version = version + 1, updated_at = CURRENT_TIMESTAMP WHERE id = ? AND version = ?")
@SQLRestriction("deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class ItemEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    @Setter
    private UserEntity owner;

    @Column(nullable = false, length = 255)
    @Setter
    private String title;

    @Column(columnDefinition = "TEXT")
    @Setter
    private String content;

    @Version
    @Column(nullable = false)
    private Integer version;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    @Setter
    private boolean deleted = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    void setCreationTimes() {
        Instant now = Instant.now();
        if (createdAt == null) {
            createdAt = now;
        }
        updatedAt = now;
    }

    @PreUpdate
    void updateModificationTime() {
        updatedAt = Instant.now();
    }

}
