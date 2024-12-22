package io.devopsnextgenx.microservices.modules.security;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import io.devopsnextgenx.microservices.modules.security.basic.BasicSecurityConfiguration;
import io.devopsnextgenx.microservices.modules.security.configuration.RestTemplateConfiguration;
import io.devopsnextgenx.microservices.modules.security.filters.RequestContextFilter;
import io.devopsnextgenx.microservices.modules.security.filters.SecurityFilterConfiguration;
import io.devopsnextgenx.microservices.modules.security.jwt.JwtSecurityConfiguration;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableConfigurationProperties
@ConditionalOnProperty(value = "app.modules.security.enabled", havingValue = "true", matchIfMissing = false)
@AutoConfigureAfter(value = {
        BasicSecurityConfiguration.class,
        JwtSecurityConfiguration.class
})
public class AppSecurityConfiguration {

    @Bean
    SecurityFilterConfiguration securityFilterConfiguration() {
        return new SecurityFilterConfiguration();
    }

    @Bean
    RestTemplateConfiguration restTemplateConfiguration() {
        return new RestTemplateConfiguration();
    }

    @Value("${app.modules.security.jwt.api.reqPath:/jwt/*}")
    private String jwtApiPath;

    @Bean
    public FilterRegistrationBean<RequestContextFilter> getRequestContextFilter() {
        FilterRegistrationBean<RequestContextFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new RequestContextFilter());
        registration.addUrlPatterns(jwtApiPath);
        registration.setOrder(5);
        registration.setName("appxRequestContextFilter");

        return registration;
    }
}
