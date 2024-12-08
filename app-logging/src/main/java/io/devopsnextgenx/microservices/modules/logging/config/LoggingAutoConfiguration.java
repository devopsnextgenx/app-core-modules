package io.devopsnextgenx.microservices.modules.logging.config;

import io.devopsnextgenx.microservices.modules.logging.config.advice.AppExceptionHandlerController;
import io.devopsnextgenx.microservices.modules.logging.config.advice.GlobalExceptionHandlerController;
import io.devopsnextgenx.microservices.modules.logging.config.advice.RestClientExceptionHandlerController;
import io.devopsnextgenx.microservices.modules.logging.config.filter.TomcatApiLoggerFilter;
import io.opentracing.Tracer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
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

    public GlobalExceptionHandlerController globalExceptionHandlerController(Tracer tracer) {
        return new GlobalExceptionHandlerController(tracer);
    }

    @Bean
    public AppExceptionHandlerController appExceptionHandlerController(Tracer tracer) {
        return new AppExceptionHandlerController(tracer);
    }

    @Bean
    public RestClientExceptionHandlerController restClientExceptionHandlerController(Tracer tracer) {
        return new RestClientExceptionHandlerController(tracer);
    }
}
