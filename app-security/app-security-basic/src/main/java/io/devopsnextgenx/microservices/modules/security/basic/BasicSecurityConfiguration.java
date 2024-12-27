package io.devopsnextgenx.microservices.modules.security.basic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import io.devopsnextgenx.microservices.modules.security.repositories.AppxUserRepositoryImpl;
import io.devopsnextgenx.microservices.modules.services.AppxUserDetailsService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableConfigurationProperties
@ConditionalOnProperty(value = "appx.modules.security.basic.enabled", havingValue = "true", matchIfMissing = true)
public class BasicSecurityConfiguration {

    @Value("${appx.modules.security.basic.user.name:admin}")
    private String username;
    @Value("${appx.modules.security.basic.user.password:password}")
    private String password;
    @Value("${appx.modules.security.basic.user.role:USER}")
    private String role;

    @Bean("userDetailsService")
    @ConditionalOnProperty(value = "appx.modules.security.basic.enabled", havingValue = "true", matchIfMissing = false)
    public AppxUserDetailsService userDetailsService(AppxUserRepositoryImpl appxUserRepositoryImpl) {
        log.info("BasicSecurityConfiguration: userDetailsService");
        return new AppxUserDetailsService(appxUserRepositoryImpl);
    }

    @Value("${appx.modules.security.basic.api.path:/basic/**}")
    private String basicApiPath;

    @Bean
    @Order(50)
    public SecurityFilterChain basicFilterChain(HttpSecurity http) throws Exception {
        log.info("SecurityFilterChain: basicFilterChain");
        http
            .securityMatcher(basicApiPath)
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(basicApiPath).authenticated()
                .anyRequest().permitAll()
            )
            .httpBasic(Customizer.withDefaults());

            // Test using below in console
            // curl -u user:password -X GET http://localhost:8080/api/demos
            // GET http://localhost:8080/basic/demos
            // Authorization: Basic dXNlcjpwYXNzd29yZA==
        
            return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        log.info("BasicSecurityConfiguration: passwordEncoder");
        return new BCryptPasswordEncoder();
    }
}
