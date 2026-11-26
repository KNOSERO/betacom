package com.ravcube.betacom.domain.port;

public interface PasswordVerifier {

    boolean matches(String rawPassword, String encodedPassword);
}
