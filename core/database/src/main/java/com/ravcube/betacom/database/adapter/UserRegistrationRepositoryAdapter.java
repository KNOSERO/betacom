package com.ravcube.betacom.database.adapter;

import com.ravcube.betacom.database.entity.UserEntity;
import com.ravcube.betacom.database.repository.UserEntityRepository;
import com.ravcube.betacom.domain.port.UserRegistrationRepository;
import com.ravcube.betacom.domain.spec.RegisteredUser;
import com.ravcube.betacom.domain.authentication.UserAlreadyExistsException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

@Repository
public class UserRegistrationRepositoryAdapter implements UserRegistrationRepository {

    private final UserEntityRepository userRepository;

    public UserRegistrationRepositoryAdapter(UserEntityRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean existsByLogin(String login) {
        return userRepository.existsByLogin(login);
    }

    @Override
    public RegisteredUser create(String login, String encodedPassword) {
        try {
            UserEntity user = userRepository.saveAndFlush(new UserEntity(login, encodedPassword));
            return new RegisteredUser(user.getId(), user.getLogin(), user.getCreatedAt());
        } catch (DataIntegrityViolationException exception) {
            throw new UserAlreadyExistsException(login);
        }
    }
}
