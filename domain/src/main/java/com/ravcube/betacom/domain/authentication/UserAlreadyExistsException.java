package com.ravcube.betacom.domain.authentication;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String login) {
        super("Login is already taken: " + login);
    }
}
