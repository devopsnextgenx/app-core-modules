package io.devopsnextgenx.microservices.modules.eureka.config;

import java.util.Arrays;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import io.devopsnextgenx.microservices.modules.eureka.models.SeedUser;
import io.devopsnextgenx.microservices.modules.eureka.models.UserList;

import io.devopsnextgenx.microservices.modules.models.Organization;
import io.devopsnextgenx.microservices.modules.models.Role;
import io.devopsnextgenx.microservices.modules.models.User;
import io.devopsnextgenx.microservices.modules.access.model.ROLE;
import io.devopsnextgenx.microservices.modules.services.AppxUserDetailsService;
import io.devopsnextgenx.microservices.modules.utils.converters.PasswordEncryptor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class SecurityConfiguration {
    @Autowired
    AppxUserDetailsService userDetailsService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @EventListener
    public void onApplicationReadyEvent(ApplicationReadyEvent event) {
        UserList appUserList = (UserList) event.getApplicationContext().getBeanFactory().getBean("appUserList");
        log.info("UserList: " + appUserList.getUsers().size());
        for (SeedUser user : appUserList.getUsers()) {
            if(!userDetailsService.existUser(user.getUsername())) {
                    User seedUser = User.builder()
                    .userName(user.getUsername())
                    .password(passwordEncoder.encode(user.getPassword()))
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .email(user.getEmail())
                    .active(true)
                    .userRoles(user.getRoles().stream().map(role -> Role.builder().name(ROLE.valueOf(role)).build()).toList())
                    .organization(
                        Organization.builder()
                        .adminId("Org-0000X")
                        .orgName("devopsnextgenx")
                        .active(true).build()
                    ).build();
                    log.info("User: " + seedUser.getUserName() + " Password: " + seedUser.getPassword());
                userDetailsService.createUser(seedUser);
            }
        }
    }

    @Bean
    @ConfigurationProperties("app.user-list")
    public UserList appUserList() {
        return new UserList();
    }
}