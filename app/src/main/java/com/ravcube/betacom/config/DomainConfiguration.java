package com.ravcube.betacom.config;

import com.ravcube.betacom.domain.*;
import com.ravcube.betacom.domain.port.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainConfiguration {

    @Bean
    public LoginProcessService loginProcessService(LoginRepository loginRepository, PasswordVerifier passwordVerifier) {
        return new LoginProcessService(loginRepository, passwordVerifier);
    }

    @Bean
    public UserRegistrationProcessService userRegistrationProcessService(
            UserRegistrationRepository userRepository,
            PasswordHasher passwordHasher) {
        return new UserRegistrationProcessService(userRepository, passwordHasher);
    }

    @Bean
    public ItemProcessService itemProcessService(ItemRepository itemRepository, SessionHelper sessionHelper) {
        return new ItemProcessService(itemRepository, sessionHelper);
    }

    @Bean
    public ItemPermissionProcessService itemPermissionProcessService(ItemRepository itemRepository, ItemPermissionRepository itemPermissionRepository, SessionHelper sessionHelper) {
        return new ItemPermissionProcessService(itemRepository, itemPermissionRepository, sessionHelper);
    }

    @Bean
    public ItemHistoryProcessService itemHistoryProcessService(
            ItemRepository itemRepository,
            ItemHistoryRepository itemHistoryRepository,
            SessionHelper sessionHelper) {
        return new ItemHistoryProcessService(itemRepository, itemHistoryRepository, sessionHelper);
    }
}
