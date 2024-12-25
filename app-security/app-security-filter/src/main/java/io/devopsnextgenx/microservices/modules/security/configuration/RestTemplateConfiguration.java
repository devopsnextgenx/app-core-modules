package io.devopsnextgenx.microservices.modules.security.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.client.RestTemplate;

import io.devopsnextgenx.microservices.modules.security.interceptors.RestTemplateInterceptor;
import io.devopsnextgenx.microservices.modules.ssl.RestTemplateConfig;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class RestTemplateConfiguration {

    @Value("${appx.modules.security.truststore.password}")
    private String trustStorePassword;

    @Value("${appx.modules.security.truststore.location}")
    private String trustStoreLocation;

    @Bean
    public RestTemplateConfig restTemplateConfig() {
        return new RestTemplateConfig();
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(RestTemplateConfig restTemplateConfig, ResourceLoader resourceLoader) throws Exception {
        RestTemplate restTemplate = restTemplateConfig.restTemplateWithTrustStore(
            trustStoreLocation,
            trustStorePassword,
            resourceLoader
        );
        log.info("RestTemplate created with appx truststore and appx RestTemplateInterceptor");
        restTemplate.getInterceptors().add(new RestTemplateInterceptor());
        return restTemplate;
    }
}
