package io.devopsnextgenx.microservices.modules.eureka.config;

import org.springframework.context.annotation.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;

import io.devopsnextgenx.microservices.modules.security.models.Organization;
import io.devopsnextgenx.microservices.modules.security.models.Role;
import io.devopsnextgenx.microservices.modules.security.models.User;
import io.devopsnextgenx.base.modules.credentials.models.AppxUser;
import io.devopsnextgenx.base.modules.credentials.models.AppxUserList;
import io.devopsnextgenx.microservices.modules.access.model.ROLE;
import io.devopsnextgenx.microservices.modules.services.AppxUserDetailsService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class AppxSecurityConfiguration {
    @Autowired
    AppxUserDetailsService userDetailsService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @EventListener
    public void onApplicationReadyEvent(ApplicationReadyEvent event) {
        AppxUserList appUserList = (AppxUserList) event.getApplicationContext().getBeanFactory().getBean("appxUserList");
        log.info("UserList: " + appUserList.getUserList().size());
        for (AppxUser user : appUserList.getUserList()) {
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
                    log.info("User: " + seedUser.getUserName() + " FirstName: " + seedUser.getFirstName());
                userDetailsService.createUser(seedUser);
            }
        }
    }
}