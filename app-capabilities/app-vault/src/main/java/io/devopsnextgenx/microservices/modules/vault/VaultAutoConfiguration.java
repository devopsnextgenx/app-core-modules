package io.devopsnextgenx.microservices.modules.vault;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.devopsnextgenx.base.modules.credentials.models.AppxUserList;

@Slf4j
@Data
@Configuration
@AutoConfiguration
@EnableConfigurationProperties
@ConditionalOnProperty(name = "appx.modules.vault.enabled", havingValue = "true", matchIfMissing = false)
@ConfigurationProperties("appx.modules.security")
public class VaultAutoConfiguration {
    
    private String jwtSecret;

    @Bean
    @ConfigurationProperties("appx.modules.security.context")
    public AppxUserList appxUserList() {
        log.info("=========================================================================");
        log.info("jwtSecret: {}", jwtSecret);
        log.info("=========================================================================");
        return AppxUserList.builder().build();
    }
}
