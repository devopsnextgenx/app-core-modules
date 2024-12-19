package io.devopsnextgenx.microservices.modules.monitoring;

import java.time.Duration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
// import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4jBulkheadProvider;
// import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// import org.springframework.boot.web.servlet.ServletContextInitializer;
// import org.springframework.boot.web.servlet.ServletRegistrationBean;
// import org.springframework.context.annotation.Bean;
// import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
// import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
// import io.github.resilience4j.timelimiter.TimeLimiterRegistry;

// import jakarta.servlet.Servlet;
// import jakarta.servlet.ServletContext;
// import jakarta.servlet.ServletException;

@Configuration
@EnableConfigurationProperties
@ConditionalOnProperty(value = "app.modules.monitoring.enabled", havingValue = "true", matchIfMissing = false)
public class AppMonitoringConfiguration {
    // @Bean
    // public ServletContextInitializer hystrixMetricsStreamInitializer() {
    // return new ServletContextInitializer() {
    // @Override
    // public void onStartup(ServletContext servletContext) throws ServletException
    // {
    // servletContext.addServlet("hystrixMetricsStreamServlet", (Class<? extends
    // Servlet>) HystrixMetricsStreamServlet.class)
    // .addMapping("/hystrix.stream");
    // }
    // };
    // }

    // @Bean
    // public ServletRegistrationBean<HystrixMetricsStreamServlet>
    // hystrixMetricsStreamServlet() {
    // ServletRegistrationBean<HystrixMetricsStreamServlet> registration =
    // new ServletRegistrationBean<>(new HystrixMetricsStreamServlet());
    // registration.setLoadOnStartup(1);
    // registration.addUrlMappings("/hystrix.stream");
    // return registration;
    // }

    @Bean
    public Customizer<Resilience4JCircuitBreakerFactory> defaultCustomizer() {
        return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
            .timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(4)).build())
            .circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
            .build());
    }
}
