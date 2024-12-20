package io.devopsnextgenx.microservices.modules.eureka.models;

import java.util.List;

import lombok.Data;

@Data
public class SeedUser {
    private String username;
    private String password;
    private List<String> roles;
    private String firstName;
    private String lastName;
    private String email;
}
