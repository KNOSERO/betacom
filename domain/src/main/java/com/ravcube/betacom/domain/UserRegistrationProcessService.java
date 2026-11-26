package com.ravcube.betacom.domain;

import com.ravcube.betacom.domain.port.PasswordHasher;
import com.ravcube.betacom.domain.port.UserRegistrationRepository;
import com.ravcube.betacom.domain.authentication.UserAlreadyExistsException;
import com.ravcube.betacom.domain.spec.RegisteredUser;

public final class UserRegistrationProcessService {

    private final UserRegistrationRepository userRepository;
    private final PasswordHasher passwordHasher;

    public UserRegistrationProcessService(
            UserRegistrationRepository userRepository,
            PasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
    }

    public RegisteredUser register(String login, String password) {
        if (userRepository.existsByLogin(login)) {
            throw new UserAlreadyExistsException(login);
        }

        return userRepository.create(login, passwordHasher.hash(password));
    }
}
