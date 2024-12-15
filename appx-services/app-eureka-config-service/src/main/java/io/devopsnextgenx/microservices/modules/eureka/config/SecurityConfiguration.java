package io.devopsnextgenx.microservices.modules.eureka.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import io.devopsnextgenx.microservices.modules.eureka.models.UserList;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class SecurityConfiguration {
    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    PasswordEncoder passwordEncoder;
    
    @EventListener
    public void onApplicationReadyEvent(ApplicationReadyEvent event) {
        UserList appUserList = (UserList) event.getApplicationContext().getBeanFactory().getBean("appUserList");
        User.UserBuilder userBuilder = User.builder();
        for (io.devopsnextgenx.microservices.modules.eureka.models.User user : appUserList.getUsers()) {
            log.info( "User: " + user.getUsername() + " Password: " + user.getPassword());
            ((InMemoryUserDetailsManager)userDetailsService).createUser(userBuilder.username(
                user.getUsername())
                .password(passwordEncoder.encode(user.getPassword()))
                .roles(user.getRole()).build());
        }
    }

    @Bean
    @ConfigurationProperties("app.user-list")
    public UserList appUserList() {
        return new UserList();
    }
}