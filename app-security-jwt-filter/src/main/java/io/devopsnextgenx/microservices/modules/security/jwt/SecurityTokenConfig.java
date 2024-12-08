package io.devopsnextgenx.microservices.modules.security.jwt;

import io.devopsnextgenx.microservices.modules.security.jwt.cache.JWTVerifierCache;
import io.devopsnextgenx.microservices.modules.security.jwt.config.SwaggerUISecurityConfig;
import io.devopsnextgenx.microservices.modules.security.jwt.config.AppConfig;
import io.devopsnextgenx.microservices.modules.security.jwt.config.OAuthApplicationsConfig;
import io.devopsnextgenx.microservices.modules.security.jwt.helpers.JwtVerifierHelper;
import io.devopsnextgenx.microservices.modules.security.jwt.helpers.OAuthLoginHelper;
import io.devopsnextgenx.microservices.modules.security.jwt.helpers.YamlPropertyLoaderFactory;
import io.devopsnextgenx.microservices.modules.security.jwt.validators.ProductionTokenValidator;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpServletResponse;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableConfigurationProperties
@PropertySources({@PropertySource(name = "oauthApplications", value = {"${APPCONFIGFILE}"}, ignoreResourceNotFound = true, factory = YamlPropertyLoaderFactory.class)})
public class SecurityTokenConfig extends WebSecurityConfiguration {

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
    public JWTVerifierCache audienceVerifierCache(OAuthLoginHelper oAuthLoginHelper, RestTemplate restTemplate, AppConfig appConfig, @Value("${app.services.userServiceHost}") String userServiceHost) {
        return new JWTVerifierCache(oAuthLoginHelper, restTemplate, new JwtVerifierHelper(), appConfig, userServiceHost);
    }

    @Bean
    @ConditionalOnMissingBean(TokenValidator.class)
    public TokenValidator tokenValidator(OAuthApplicationsConfig oAuthApplicationsConfig, JWTVerifierCache jwtVerifierCache) {
        Assert.notNull(oAuthApplicationsConfig, String.format("Missing required configurations for OAuth security applications"));
        Assert.notEmpty(oAuthApplicationsConfig.getApplications(), "Configurations for OAuth security applications is empty");
        return new ProductionTokenValidator(oAuthApplicationsConfig, jwtVerifierCache, new JwtVerifierHelper());
    }

    @Bean
    public JwtTokenAuthenticationFilter jwtTokenAuthenticationFilter(TokenValidator tokenValidator) {
        log.info("security-jwt-filter: Injected TokenValidator type: '{}' ", tokenValidator.getClass().getSimpleName());
        return new JwtTokenAuthenticationFilter(tokenValidator);
    }

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

    // @Configuration
    // @Order(1)
    // public static class SwaggerActuatorBasicSecurityConfig extends WebSecurityConfiguration {

    //     @Override
    //     protected void configure(HttpSecurity http) throws Exception {
    //         http.csrf().disable()
    //                 .antMatcher("/swagger**")
    //                 .authorizeRequests()
    //                 .anyRequest().hasRole("ADMIN")
    //                 .and()
    //                 .httpBasic();
    //     }
    // }

    // @Configuration
    // @Order(2)
    // public class ApiJwtSecurityConfig extends WebSecurityConfiguration {

    //     @Autowired
    //     @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    //     private JwtTokenAuthenticationFilter jwtTokenAuthenticationFilter;

    //     @Override
    //     protected void configure(HttpSecurity http) throws Exception {
    //         http.sessionManagement()
    //                 .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    //                 .and()
    //                 .csrf().disable()
    //                 .antMatcher("/api/**")

    //                 // handle an authorized attempts
    //                 .exceptionHandling().authenticationEntryPoint((req, rsp, e) -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED))
    //                 .and()
    //                 // Add a filter to validate the tokens with every request
    //                 .addFilterAfter(jwtTokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
    //                 // authorization requests config
    //                 .authorizeRequests().antMatchers("/api/**")
    //                 .authenticated();
    //     }
    // }

    // @Configuration
    // @Order(3)
    // public class HystrixNoAuthSecurityConfig extends WebSecurityConfiguration {
    //     @Override
    //     public void configure(WebSecurity web) {
    //         web.ignoring().antMatchers("/actuator/hystrix.stream");
    //         web.ignoring().antMatchers("/turbine.stream");
    //         web.ignoring().antMatchers("/actuator/health");
    //         web.ignoring().antMatchers("/HealthCheck");
    //         web.ignoring().antMatchers("/health");
    //         web.ignoring().antMatchers("/metrics");
    //     }
    // }
}
