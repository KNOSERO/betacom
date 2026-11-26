package com.ravcube.betacom.domain.port;

public interface PasswordHasher {

    String hash(String rawPassword);
}
