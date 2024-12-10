package io.devopsnextgenx.microservices.modules.logging.config;

import io.devopsnextgenx.microservices.modules.logging.config.advice.AppExceptionHandlerController;
import io.devopsnextgenx.microservices.modules.logging.config.advice.GlobalExceptionHandlerController;
import io.devopsnextgenx.microservices.modules.logging.config.advice.RestClientExceptionHandlerController;
import io.devopsnextgenx.microservices.modules.logging.config.filter.TomcatApiLoggerFilter;
import io.devopsnextgenx.microservices.modules.tracing.TracingAutoConfiguration;
import io.opentracing.Tracer;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;

@Slf4j
@AutoConfiguration
@EnableConfigurationProperties
@AutoConfigureAfter(TracingAutoConfiguration.class)
public class LoggingAutoConfiguration {
    @Value("${spring.application.name}")
    private String applicationName;

    @Bean
    public FilterRegistrationBean<TomcatApiLoggerFilter> tomcatApiLoggerFilterRegistrationBean() {
        FilterRegistrationBean<TomcatApiLoggerFilter> registrationBean = new FilterRegistrationBean<>();
        TomcatApiLoggerFilter tomcatApiLoggerFilter = new TomcatApiLoggerFilter(applicationName);

        registrationBean.setFilter(tomcatApiLoggerFilter);
        registrationBean.addUrlPatterns("/api/*");
        registrationBean.setOrder(10);
        return registrationBean;
    }

    @Bean
    @DependsOn("tracer")
    public GlobalExceptionHandlerController globalExceptionHandlerController(Tracer tracer) {
        return new GlobalExceptionHandlerController(tracer);
    }

    @Bean
    @DependsOn("tracer")
    public AppExceptionHandlerController appExceptionHandlerController(Tracer tracer) {
        return new AppExceptionHandlerController(tracer);
    }

    @Bean
    @DependsOn("tracer")
    public RestClientExceptionHandlerController restClientExceptionHandlerController(Tracer tracer) {
        return new RestClientExceptionHandlerController(tracer);
    }
}
