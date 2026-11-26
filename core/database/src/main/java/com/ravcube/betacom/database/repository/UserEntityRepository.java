package com.ravcube.betacom.database.repository;

import com.ravcube.betacom.database.entity.UserEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, UUID> {

    boolean existsByLogin(String login);

    Optional<UserEntity> findByLogin(String login);
}
