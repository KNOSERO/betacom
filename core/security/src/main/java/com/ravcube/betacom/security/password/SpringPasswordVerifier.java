package com.ravcube.betacom.security.password;

import com.ravcube.betacom.domain.port.PasswordVerifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SpringPasswordVerifier implements PasswordVerifier {

    private final PasswordEncoder passwordEncoder;

    public SpringPasswordVerifier(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
