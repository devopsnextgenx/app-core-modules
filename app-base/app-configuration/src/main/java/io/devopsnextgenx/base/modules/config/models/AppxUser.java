package io.devopsnextgenx.base.modules.config.models;

import java.util.List;

import org.springframework.context.annotation.Configuration;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@Configuration
@NoArgsConstructor
public class AppxUser {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private List<String> roles;
}
