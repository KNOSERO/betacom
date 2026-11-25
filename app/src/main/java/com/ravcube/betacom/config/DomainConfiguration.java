package com.ravcube.betacom.config;

import com.ravcube.betacom.domain.ItemRepository;
import com.ravcube.betacom.domain.ItemProcessService;
import com.ravcube.betacom.domain.SessionHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainConfiguration {

    @Bean
    public ItemProcessService itemService(ItemRepository itemRepository, SessionHelper sessionHelper) {
        return new ItemProcessService(itemRepository, sessionHelper);
    }
}
