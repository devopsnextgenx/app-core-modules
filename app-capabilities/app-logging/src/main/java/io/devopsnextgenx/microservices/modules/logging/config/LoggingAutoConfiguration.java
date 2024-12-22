package io.devopsnextgenx.microservices.modules.logging.config;

import io.devopsnextgenx.microservices.modules.logging.config.advice.AppExceptionHandlerController;
import io.devopsnextgenx.microservices.modules.logging.config.advice.GlobalExceptionHandlerController;
import io.devopsnextgenx.microservices.modules.logging.config.advice.RestClientExceptionHandlerController;
import io.devopsnextgenx.microservices.modules.logging.config.filter.TomcatApiLoggerFilter;
import io.devopsnextgenx.microservices.modules.tracing.TracingAutoConfiguration;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@EnableConfigurationProperties
@AutoConfigureAfter(TracingAutoConfiguration.class)
@ConditionalOnProperty(value = "app.modules.logging.enabled", havingValue = "true", matchIfMissing = true)
public class LoggingAutoConfiguration {
    @Value("${spring.application.name}")
    private String applicationName;

    public LoggingAutoConfiguration() {
        log.info("LoggingAutoConfiguration loaded");
    }

    @Bean
    public FilterRegistrationBean<TomcatApiLoggerFilter> tomcatApiLoggerFilterRegistrationBean() {
        FilterRegistrationBean<TomcatApiLoggerFilter> registrationBean = new FilterRegistrationBean<>();
        TomcatApiLoggerFilter tomcatApiLoggerFilter = new TomcatApiLoggerFilter(applicationName);

        registrationBean.setFilter(tomcatApiLoggerFilter);
        registrationBean.addUrlPatterns("/api/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }

    @Bean
    public GlobalExceptionHandlerController globalExceptionHandlerController() {
        return new GlobalExceptionHandlerController();
    }

    @Bean
    public AppExceptionHandlerController appExceptionHandlerController() {
        return new AppExceptionHandlerController();
    }

    @Bean
    public RestClientExceptionHandlerController restClientExceptionHandlerController() {
        return new RestClientExceptionHandlerController();
    }
}
