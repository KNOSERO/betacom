package com.ravcube.betacom.domain;

import com.ravcube.betacom.domain.authentication.UserAlreadyExistsException;
import com.ravcube.betacom.domain.port.PasswordHasher;
import com.ravcube.betacom.domain.port.UserRegistrationRepository;
import com.ravcube.betacom.domain.spec.RegisteredUser;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserRegistrationProcessServiceTest {
    private final UserRegistrationRepository users = mock(UserRegistrationRepository.class);
    private final PasswordHasher passwordHasher = mock(PasswordHasher.class);
    private final UserRegistrationProcessService service = new UserRegistrationProcessService(users, passwordHasher);

    @Test
    void givenUnusedLogin_whenRegistering_thenUserIsCreatedWithHashedPassword() {
        // Given
        UUID userId = UUID.randomUUID();
        RegisteredUser expected = new RegisteredUser(userId, "rafal", Instant.now());
        when(users.existsByLogin("rafal")).thenReturn(false);
        when(passwordHasher.hash("secret")).thenReturn("encoded-password");
        when(users.create("rafal", "encoded-password")).thenReturn(expected);

        // When
        RegisteredUser actual = service.register("rafal", "secret");

        // Then
        assertEquals(expected, actual);
        verify(users).create("rafal", "encoded-password");
    }

    @Test
    void givenLoginAlreadyInUse_whenRegistering_thenRegistrationIsRejected() {
        // Given
        when(users.existsByLogin("rafal")).thenReturn(true);

        // When / Then
        assertThrows(UserAlreadyExistsException.class, () -> service.register("rafal", "secret"));
        verifyNoInteractions(passwordHasher);
        verify(users, never()).create(anyString(), anyString());
    }
}
