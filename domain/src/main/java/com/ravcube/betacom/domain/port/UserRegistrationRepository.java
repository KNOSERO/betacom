package com.ravcube.betacom.domain.port;

import com.ravcube.betacom.domain.spec.RegisteredUser;

public interface UserRegistrationRepository {

    boolean existsByLogin(String login);

    RegisteredUser create(String login, String encodedPassword);
}
