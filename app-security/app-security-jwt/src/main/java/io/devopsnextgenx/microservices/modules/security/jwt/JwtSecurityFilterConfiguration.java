package io.devopsnextgenx.microservices.modules.security.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@ConditionalOnProperty(value = "app.modules.security.jwt.enabled", havingValue = "true", matchIfMissing = false)
public class JwtSecurityFilterConfiguration {
    
    private TokenValidator tokenValidator;

    public JwtSecurityFilterConfiguration(TokenValidator tokenValidator) {
        this.tokenValidator = tokenValidator;
    }

    @Value("${app.modules.security.jwt.api.path:/jwt/**}")
    private String jwtApiPath;

    @Bean
    @Order(100)
    public SecurityFilterChain jwtSecurityFilterChain(HttpSecurity http) throws Exception {
        log.info("Enabling JWT security filter");
        http.securityMatcher(jwtApiPath).sessionManagement(management -> management
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csrf -> csrf.disable())
                // handle an authorized attempts
                .exceptionHandling(handling -> handling
                        .authenticationEntryPoint(
                                (req, rsp, e) -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED)))
                // Add a filter to validate the tokens with every request
                .addFilterAfter(new JwtTokenAuthenticationFilter(tokenValidator), UsernamePasswordAuthenticationFilter.class)
                // authorization requests config
                .authorizeHttpRequests(request -> request.requestMatchers(jwtApiPath)
                        .authenticated());
        return http.build();
    }
}
