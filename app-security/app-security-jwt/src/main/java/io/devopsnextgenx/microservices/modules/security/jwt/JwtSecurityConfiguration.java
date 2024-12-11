package io.devopsnextgenx.microservices.modules.security.jwt;

import io.devopsnextgenx.base.modules.config.YamlPropertyLoaderFactory;
import io.devopsnextgenx.microservices.modules.security.jwt.cache.JWTVerifierCache;
import io.devopsnextgenx.microservices.modules.security.jwt.config.SwaggerUISecurityConfig;
import io.devopsnextgenx.microservices.modules.security.jwt.config.AppConfig;
import io.devopsnextgenx.microservices.modules.security.jwt.config.OAuthApplicationsConfig;
import io.devopsnextgenx.microservices.modules.security.jwt.helpers.JwtVerifierHelper;
import io.devopsnextgenx.microservices.modules.security.jwt.helpers.OAuthLoginHelper;
import io.devopsnextgenx.microservices.modules.security.jwt.validators.ProductionTokenValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableConfigurationProperties
@PropertySources({
    @PropertySource(name = "oauthApplications", 
        value = { "${APPCONFIGFILE}" },
        ignoreResourceNotFound = true,
        factory = YamlPropertyLoaderFactory.class) 
})
@ConditionalOnProperty(value = "app.modules.security.jwt.enabled", havingValue = "true", matchIfMissing = true)
public class JwtSecurityConfiguration {
    
    @Bean
    @ConfigurationProperties("app.oauth")
    public OAuthApplicationsConfig oauthApplicationsConfig() {
        return new OAuthApplicationsConfig();
    }

    @Bean
    @ConfigurationProperties(prefix = "app.auth.app.userservice")
    public AppConfig appConfig() {
        return new AppConfig();
    }

    @Bean
    @ConfigurationProperties(prefix = "app.auth.swagger")
    public SwaggerUISecurityConfig swaggerUISecurityConfig() {
        return new SwaggerUISecurityConfig();
    }

    @Bean
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public OAuthLoginHelper oAuthLoginHelper(OAuthApplicationsConfig oAuthApplicationsConfig) {
        return new OAuthLoginHelper(oAuthApplicationsConfig);
    }

    @Bean
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public JWTVerifierCache audienceVerifierCache(OAuthLoginHelper oAuthLoginHelper, RestTemplate restTemplate,
            AppConfig appConfig, @Value("${app.services.userServiceHost}") String userServiceHost) {
        return new JWTVerifierCache(oAuthLoginHelper, restTemplate, new JwtVerifierHelper(), appConfig,
                userServiceHost);
    }

    @Bean
    @ConditionalOnMissingBean(TokenValidator.class)
    public TokenValidator tokenValidator(OAuthApplicationsConfig oAuthApplicationsConfig,
            JWTVerifierCache jwtVerifierCache) {
        Assert.notNull(oAuthApplicationsConfig,
                String.format("Missing required configurations for OAuth security applications"));
        Assert.notEmpty(oAuthApplicationsConfig.getApplications(),
                "Configurations for OAuth security applications is empty");
        return new ProductionTokenValidator(oAuthApplicationsConfig, jwtVerifierCache, new JwtVerifierHelper());
    }

    @SuppressWarnings("deprecation")
    @Bean
    @ConditionalOnMissingBean(UserDetailsService.class)
    public UserDetailsService userDetailsService(SwaggerUISecurityConfig appSwaggerUISecurityConfig) {
        User.UserBuilder users = User.withDefaultPasswordEncoder();
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(users.username(
                appSwaggerUISecurityConfig.getSwaggerUser())
                .password(appSwaggerUISecurityConfig.getSwaggerUserPassword())
                .roles("USER").build());
        manager.createUser(users.username(
                appSwaggerUISecurityConfig.getSwaggerAdmin())
                .password(appSwaggerUISecurityConfig.getSwaggerAdminPassword())
                .roles("USER", "ADMIN").build());
        return manager;
    }

    @Bean
    public JwtSecurityFilterConfiguration JwtSecurityFilterConfiguration(TokenValidator tokenValidator) {
        return new JwtSecurityFilterConfiguration(tokenValidator);
    }

    @Order(3)
    @Configuration
    public class HystrixNoAuthSecurityConfig {
        @Bean
        public WebSecurityCustomizer webSecurityCustomizer() {
            return (web) -> web.ignoring().requestMatchers(
                "/actuator/hystrix.stream",
                "/turbine.stream",
                "/actuator/health",
                "/HealthCheck",
                "/health",
                "/metrics",
                "/actuator"
            );
        }
    }
}
