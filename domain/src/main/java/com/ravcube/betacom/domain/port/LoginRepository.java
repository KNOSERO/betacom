package com.ravcube.betacom.domain.port;

import com.ravcube.betacom.domain.spec.LoginCredential;
import java.util.Optional;

public interface LoginRepository {

    Optional<LoginCredential> findCredentialsByLogin(String login);
}
