package com.ravcube.betacom.domain;

import com.ravcube.betacom.domain.port.LoginRepository;
import com.ravcube.betacom.domain.port.PasswordVerifier;
import com.ravcube.betacom.domain.authentication.InvalidCredentialsException;
import com.ravcube.betacom.domain.spec.AuthenticatedUser;
import com.ravcube.betacom.domain.spec.LoginCredential;

public final class LoginProcessService {

    private final LoginRepository loginRepository;
    private final PasswordVerifier passwordVerifier;

    public LoginProcessService(LoginRepository loginRepository, PasswordVerifier passwordVerifier) {
        this.loginRepository = loginRepository;
        this.passwordVerifier = passwordVerifier;
    }

    public AuthenticatedUser authenticate(String login, String password) {
        if (login == null || login.isBlank() || password == null || password.isBlank()) {
            throw new InvalidCredentialsException();
        }

        LoginCredential credential = loginRepository.findCredentialsByLogin(login)
                .orElseThrow(InvalidCredentialsException::new);
        if (!passwordVerifier.matches(password, credential.encodedPassword())) {
            throw new InvalidCredentialsException();
        }

        return new AuthenticatedUser(credential.userId(), credential.login());
    }
}
