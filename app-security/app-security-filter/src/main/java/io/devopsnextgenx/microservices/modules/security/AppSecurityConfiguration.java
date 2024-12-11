package io.devopsnextgenx.microservices.modules.security;

import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import io.devopsnextgenx.microservices.modules.security.configuration.RestTemplateConfiguration;
import io.devopsnextgenx.microservices.modules.security.filter.SecurityFilterConfiguration;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableConfigurationProperties
@ConditionalOnProperty(value = "app.modules.security.enabled", havingValue = "true", matchIfMissing = true)
public class AppSecurityConfiguration {

    @Bean
    SecurityFilterConfiguration securityFilterConfiguration() {
        return new SecurityFilterConfiguration();
    }

    @Bean
    RestTemplateConfiguration restTemplateConfiguration() {
        return new RestTemplateConfiguration();
    }
}
