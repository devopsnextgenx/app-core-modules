package io.devopsnextgenx.microservices.modules.security.basic;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableConfigurationProperties
@ConditionalOnProperty(value = "app.modules.security.basic.enabled", havingValue = "true", matchIfMissing = true)
public class BasicSecurityConfiguration {

    @Value("${app.modules.security.basic.user.name:user}")
    private String username;
    @Value("${app.modules.security.basic.user.password:password}")
    private String password;
    @Value("${app.modules.security.basic.user.role:USER}")
    private String role;

    @Bean
    @ConditionalOnMissingBean(name = "userDetailsService")
    @ConditionalOnProperty(value = "app.modules.security.basic.user.enabled", havingValue = "true", matchIfMissing = false)
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        log.info("BasicSecurityConfiguration: userDetailsService");
        User.UserBuilder userBuilder = User.builder();
        InMemoryUserDetailsManager userDetailsManager = new InMemoryUserDetailsManager();
        userDetailsManager.createUser(userBuilder.username(
            username)
            .password(passwordEncoder.encode(password))
            .roles(role).build());
        return userDetailsManager;
    }

    @Bean
    @ConditionalOnMissingBean(PasswordEncoder.class)
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Value("${app.modules.security.basic.api.path:/basic/**}")
    private String basicApiPath;

    @Bean
    @Order(50)
    public SecurityFilterChain basicFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher(basicApiPath)
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(basicApiPath).authenticated()
                .anyRequest().permitAll()
            )
            .httpBasic(Customizer.withDefaults())
            .exceptionHandling(handling -> handling
                .authenticationEntryPoint((request, response, authException) -> {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                })
            );
        
            // Test using below in console
            // curl -u user:password -X GET http://localhost:8080/api/demos
            // GET http://localhost:8080/basic/demos
            // Authorization: Basic dXNlcjpwYXNzd29yZA==
        
            return http.build();
    }
}
