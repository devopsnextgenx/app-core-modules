package io.devopsnextgenx.microservices.modules.security.filter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableConfigurationProperties
public class SecurityFilterConfiguration {

    // Configuration for public/unsecured endpoints
    @Bean
    @Order(10)
    public SecurityFilterChain publicFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/public/**", "/login", "/register")
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/public/**").permitAll()
                .requestMatchers("/login").permitAll()
                .requestMatchers("/register").permitAll()
            )
            .csrf(csrf -> csrf.disable()); // Optionally disable CSRF for public endpoints

        return http.build();
    }

    // Default configuration for all other endpoints
    @Bean
    @Order(3)
    public SecurityFilterChain defaultFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/api/**").authenticated()
                .anyRequest().denyAll()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/dashboard")
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .deleteCookies("JSESSIONID")
            );

        return http.build();
    }
    
    // @Bean
    // @Order(20)
    // public SecurityFilterChain adminFilterChain(HttpSecurity http) throws Exception {
    //     http
    //         .securityMatcher("/admin/**")
    //         .authorizeHttpRequests(authorize -> authorize
    //             .requestMatchers("/admin/**").hasRole("ADMIN")
    //         )
    //         .httpBasic(); // Use HTTP Basic authentication for admin endpoints

    //     return http.build();
    // }

    // @Bean
    // @Order(30)
    // public SecurityFilterChain actuatorFilterChain(HttpSecurity http) throws Exception {
    //     http
    //         .securityMatcher("/actuator/**")
    //         .authorizeHttpRequests(authorize -> authorize
    //             .requestMatchers("/actuator/**").hasRole("ADMIN")
    //         )
    //         .httpBasic(); // Use HTTP Basic authentication for admin endpoints

    //     return http.build();
    // }

    // @Bean
    // @Order(40)
    // public SecurityFilterChain managementFilterChain(HttpSecurity http) throws Exception {
    //     http
    //         .securityMatcher("/management/**")
    //         .authorizeHttpRequests(authorize -> authorize
    //             .requestMatchers("/management/**").hasRole("ADMIN")
    //         )
    //         .httpBasic(); // Use HTTP Basic authentication for admin endpoints

    //     return http.build();
    // }

    // @Bean
    // @Order(40)
    // public SecurityFilterChain filterChain(HttpSecurity http, TokenValidator tokenValidator) throws Exception {
    //     http.sessionManagement(management -> management
    //             .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
    //             .csrf(csrf -> csrf.disable())
    //             // handle an authorized attempts
    //             .exceptionHandling(handling -> handling
    //                     .authenticationEntryPoint(
    //                             (req, rsp, e) -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED)))
    //             // Add a filter to validate the tokens with every request
    //             .addFilterAfter(new JwtTokenAuthenticationFilter(tokenValidator), UsernamePasswordAuthenticationFilter.class)
    //             // authorization requests config
    //             .authorizeHttpRequests(request -> request.requestMatchers("/api/**")
    //                     .authenticated());
    //     return http.build();
    // }

    // Default configuration for all other endpoints
    // @Bean
    // @Order(50)
    // public SecurityFilterChain defaultFilterChain(HttpSecurity http) throws Exception {
    //     http
    //         .authorizeHttpRequests(authorize -> authorize
    //             .requestMatchers("/api/**").authenticated()
    //             .anyRequest().denyAll()
    //         )
    //         .formLogin(form -> form
    //             .loginPage("/login")
    //             .defaultSuccessUrl("/dashboard")
    //         )
    //         .logout(logout -> logout
    //             .logoutSuccessUrl("/login?logout")
    //             .deleteCookies("JSESSIONID")
    //         );

    //     return http.build();
    // }
}
