package com.ravcube.betacom.database.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.Instant;
import java.util.UUID;

import lombok.*;

@Entity
@Table(name = "item_permissions", uniqueConstraints =
        @UniqueConstraint(name = "uk_item_permissions_item_user", columnNames = {"item_id", "user_id"}))
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemPermissionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "item_id", nullable = false)
    @Setter
    private ItemEntity item;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @Setter
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    @Setter
    private PermissionRole role;

    @Column(name = "granted_at", nullable = false, updatable = false)
    private Instant grantedAt;

    @PrePersist
    void setGrantTime() {
        if (grantedAt == null) {
            grantedAt = Instant.now();
        }
    }

}
