package io.devopsnextgenx.microservices.modules.loader.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.devopsnextgenx.microservices.modules.security.configuration.AuthServiceProperties;

@Configuration
@EnableConfigurationProperties
public class DataloadAutoConfiguration {
    
    @Bean
    @ConditionalOnMissingBean
    @ConfigurationProperties("appx.modules.security.dataloader")
    public DataLoaderProperties dataLoaderProperties(){
        return new DataLoaderProperties();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConfigurationProperties("appx.modules.security.auth")
    public AuthServiceProperties authServiceProperties(){
        return new AuthServiceProperties();
    }
}
