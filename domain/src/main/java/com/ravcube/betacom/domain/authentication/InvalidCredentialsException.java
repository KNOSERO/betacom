package com.ravcube.betacom.domain.authentication;

public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException() {
        super("Invalid login or password");
    }
}
