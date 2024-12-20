package io.devopsnextgenx.microservices.modules.eureka.models;

import java.util.List;

import lombok.Data;

@Data
public class UserList {
    private List<SeedUser> users;
}
