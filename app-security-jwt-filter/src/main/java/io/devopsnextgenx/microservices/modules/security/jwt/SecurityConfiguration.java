package io.devopsnextgenx.microservices.modules.security.jwt;

import io.devopsnextgenx.microservices.modules.security.jwt.cache.JWTVerifierCache;
import io.devopsnextgenx.microservices.modules.security.jwt.config.SwaggerUISecurityConfig;
import io.devopsnextgenx.microservices.modules.security.jwt.config.AppConfig;
import io.devopsnextgenx.microservices.modules.security.jwt.config.OAuthApplicationsConfig;
import io.devopsnextgenx.microservices.modules.security.jwt.helpers.JwtVerifierHelper;
import io.devopsnextgenx.microservices.modules.security.jwt.helpers.OAuthLoginHelper;
import io.devopsnextgenx.microservices.modules.security.jwt.helpers.YamlPropertyLoaderFactory;
import io.devopsnextgenx.microservices.modules.security.jwt.validators.ProductionTokenValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import org.springframework.security.config.Customizer;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableConfigurationProperties
@PropertySources({ @PropertySource(name = "oauthApplications", value = {
        "${APPCONFIGFILE}" }, ignoreResourceNotFound = true, factory = YamlPropertyLoaderFactory.class) })
public class SecurityConfiguration {

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

    @Bean
    public JwtTokenAuthenticationFilter jwtTokenAuthenticationFilter(TokenValidator tokenValidator) {
        log.info("security-jwt-filter: Injected TokenValidator type: '{}' ", tokenValidator.getClass().getSimpleName());
        return new JwtTokenAuthenticationFilter(tokenValidator);
    }

    @SuppressWarnings("deprecation")
    @Bean
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

    @Configuration
    @Order(1)
    public static class SwaggerActuatorBasicSecurityConfig {

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http
                    .authorizeHttpRequests((authz) -> authz
                            .anyRequest().authenticated())
                    .httpBasic(Customizer.withDefaults());
            return http.build();
        }
    }

    @Configuration
    @Order(2)
    public class ApiJwtSecurityConfig {

        @Autowired
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
        private JwtTokenAuthenticationFilter jwtTokenAuthenticationFilter;

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http.sessionManagement(management -> management
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .csrf(csrf -> csrf.disable())
                    // handle an authorized attempts
                    .exceptionHandling(handling -> handling
                            .authenticationEntryPoint(
                                    (req, rsp, e) -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED)))
                    // Add a filter to validate the tokens with every request
                    .addFilterAfter(jwtTokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                    // authorization requests config
                    .authorizeHttpRequests(request -> request.requestMatchers("/api/**")
                            .authenticated());
            return http.build();
        }
    }

    @Configuration
    @Order(3)
    public class HystrixNoAuthSecurityConfig {
        @Bean
        public WebSecurityCustomizer webSecurityCustomizer() {
            return (web) -> web.ignoring().requestMatchers("/actuator/hystrix.stream", "/turbine.stream",
                    "/actuator/health", "/HealthCheck", "/health", "/metrics");
        }
    }
}
