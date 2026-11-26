package com.ravcube.betacom.service;

import com.ravcube.betacom.api.dto.LoginRequestDTO;
import com.ravcube.betacom.api.dto.LoginResponseDTO;
import com.ravcube.betacom.api.dto.RegisterRequestDTO;
import com.ravcube.betacom.api.dto.RegisterResponseDTO;
import com.ravcube.betacom.domain.LoginProcessService;
import com.ravcube.betacom.domain.UserRegistrationProcessService;
import com.ravcube.betacom.domain.spec.AuthenticatedUser;
import com.ravcube.betacom.domain.spec.RegisteredUser;
import com.ravcube.betacom.security.jwt.JwtSecurityProperties;
import com.ravcube.betacom.security.jwt.JwtTokenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRegistrationProcessService userRegistrationProcessService;
    private final JwtTokenService jwtTokenService;
    private final JwtSecurityProperties jwtProperties;
    private final LoginProcessService loginProcessService;

    public AuthService(
            UserRegistrationProcessService userRegistrationProcessService,
            JwtTokenService jwtTokenService,
            JwtSecurityProperties jwtProperties,
            LoginProcessService loginProcessService) {
        this.userRegistrationProcessService = userRegistrationProcessService;
        this.jwtTokenService = jwtTokenService;
        this.jwtProperties = jwtProperties;
        this.loginProcessService = loginProcessService;
    }

    @Transactional
    public RegisterResponseDTO register(RegisterRequestDTO request) {
        RegisteredUser user = userRegistrationProcessService.register(request.login(), request.password());
        return new RegisterResponseDTO(user.userId(), user.login(), user.createdAt());
    }

    @Transactional(readOnly = true)
    public LoginResponseDTO login(LoginRequestDTO request) {
        AuthenticatedUser user = loginProcessService.authenticate(request.login(), request.password());

        return new LoginResponseDTO(
                jwtTokenService.issueToken(user.userId(), user.login()),
                jwtProperties.expiration().toSeconds());
    }
}
