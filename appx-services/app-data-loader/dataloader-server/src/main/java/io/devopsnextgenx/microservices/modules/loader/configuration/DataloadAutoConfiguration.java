package io.devopsnextgenx.microservices.modules.loader.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
public class DataloadAutoConfiguration {
    
    @Bean
    @ConditionalOnMissingBean
    @ConfigurationProperties("app.security.auth")
    public DataLoaderProperties dataLoaderProperties(){
        return new DataLoaderProperties();
    }
}
