package io.devopsnextgenx.microservices.modules.security.basic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import io.devopsnextgenx.microservices.modules.repositories.AppxUserRepositoryImpl;
import io.devopsnextgenx.microservices.modules.services.AppxUserDetailsService;
import io.devopsnextgenx.microservices.modules.utils.converters.PasswordEncryptor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableConfigurationProperties
@ConditionalOnProperty(value = "app.modules.security.basic.enabled", havingValue = "true", matchIfMissing = true)
public class BasicSecurityConfiguration {

    @Value("${app.modules.security.basic.user.name:admin}")
    private String username;
    @Value("${app.modules.security.basic.user.password:p@ssw0rd}")
    private String password;
    @Value("${app.modules.security.basic.user.role:USER}")
    private String role;

    @Bean
    @ConditionalOnMissingBean(name = "userDetailsService")
    @ConditionalOnProperty(value = "app.modules.security.basic.user.enabled", havingValue = "true", matchIfMissing = false)
    public UserDetailsService userDetailsService(AppxUserRepositoryImpl appxUserRepositoryImpl) {
        log.info("BasicSecurityConfiguration: userDetailsService");
        return new AppxUserDetailsService(appxUserRepositoryImpl);
    }

    @Value("${app.modules.security.basic.api.path:/basic/**}")
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
    public static NoOpPasswordEncoder passwordEncoder() {
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }
    

    // @Bean
    // public PasswordEncoder passwordEncoder() {
    //     log.info("BasicSecurityConfiguration: passwordEncoder");
    //     return new BCryptPasswordEncoder();
    // }

    @Autowired
    @DependsOn("passwordEncoder")
    public void configureGlobal(AuthenticationManagerBuilder auth, AppxUserDetailsService appxUserDetailsService, PasswordEncoder passwordEncoder, DataSource datasource) throws Exception {
        log.info("BasicSecurityConfiguration: configureGlobal : {}", passwordEncoder.getClass().getName());
        auth.userDetailsService(appxUserDetailsService).passwordEncoder(passwordEncoder);
        auth.jdbcAuthentication().dataSource(datasource);
    }
}
