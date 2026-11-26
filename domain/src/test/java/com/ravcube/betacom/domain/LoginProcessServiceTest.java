package com.ravcube.betacom.domain;

import com.ravcube.betacom.domain.authentication.InvalidCredentialsException;
import com.ravcube.betacom.domain.port.LoginRepository;
import com.ravcube.betacom.domain.port.PasswordVerifier;
import com.ravcube.betacom.domain.spec.LoginCredential;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginProcessServiceTest {
    private final LoginRepository loginRepository = mock(LoginRepository.class);
    private final PasswordVerifier passwordVerifier = mock(PasswordVerifier.class);
    private final LoginProcessService service = new LoginProcessService(loginRepository, passwordVerifier);

    @Test
    void givenValidCredentials_whenAuthenticating_thenAuthenticatedUserIsReturned() {
        // Given
        UUID userId = UUID.randomUUID();
        when(loginRepository.findCredentialsByLogin("rafal"))
                .thenReturn(Optional.of(new LoginCredential(userId, "rafal", "encoded-password")));
        when(passwordVerifier.matches("correct-password", "encoded-password")).thenReturn(true);

        // When
        var actual = service.authenticate("rafal", "correct-password");

        // Then
        assertEquals(userId, actual.userId());
        assertEquals("rafal", actual.login());
    }

    @Test
    void givenBlankLogin_whenAuthenticating_thenCredentialsAreRejectedBeforeLookup() {
        // Given / When / Then
        assertThrows(InvalidCredentialsException.class, () -> service.authenticate(" ", "secret"));
        verifyNoInteractions(loginRepository, passwordVerifier);
    }

    @Test
    void givenNullPassword_whenAuthenticating_thenCredentialsAreRejectedBeforeLookup() {
        // Given / When / Then
        assertThrows(InvalidCredentialsException.class, () -> service.authenticate("rafal", null));
        verifyNoInteractions(loginRepository, passwordVerifier);
    }

    @Test
    void givenUnknownLogin_whenAuthenticating_thenCredentialsAreRejected() {
        // Given
        when(loginRepository.findCredentialsByLogin("missing")).thenReturn(Optional.empty());

        // When / Then
        assertThrows(InvalidCredentialsException.class, () -> service.authenticate("missing", "secret"));
        verifyNoInteractions(passwordVerifier);
    }

    @Test
    void givenIncorrectPassword_whenAuthenticating_thenCredentialsAreRejected() {
        // Given
        when(loginRepository.findCredentialsByLogin("rafal"))
                .thenReturn(Optional.of(new LoginCredential(UUID.randomUUID(), "rafal", "encoded-password")));
        when(passwordVerifier.matches("wrong-password", "encoded-password")).thenReturn(false);

        // When / Then
        assertThrows(InvalidCredentialsException.class, () -> service.authenticate("rafal", "wrong-password"));
    }
}
