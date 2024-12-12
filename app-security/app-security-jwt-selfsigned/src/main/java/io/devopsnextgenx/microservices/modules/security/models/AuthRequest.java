package io.devopsnextgenx.microservices.modules.security.models;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
}