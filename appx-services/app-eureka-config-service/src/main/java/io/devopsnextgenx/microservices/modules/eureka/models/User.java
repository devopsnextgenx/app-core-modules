package io.devopsnextgenx.microservices.modules.eureka.models;

import lombok.Data;

@Data
public class User {
    private String username;
    private String password;
    private String role;
}
