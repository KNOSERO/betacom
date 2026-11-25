package com.ravcube.betacom.config;

import com.ravcube.betacom.domain.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainConfiguration {

    @Bean
    public ItemProcessService itemService(ItemRepository itemRepository, SessionHelper sessionHelper) {
        return new ItemProcessService(itemRepository, sessionHelper);
    }

    @Bean
    public ItemPermissionProcessService itemPermissionProcessService(ItemRepository itemRepository, ItemPermissionRepository itemPermissionRepository, SessionHelper sessionHelper) {
        return new ItemPermissionProcessService(itemRepository, itemPermissionRepository, sessionHelper);
    }
}
