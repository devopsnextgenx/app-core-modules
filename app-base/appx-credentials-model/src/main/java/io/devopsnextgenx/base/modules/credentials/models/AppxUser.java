package io.devopsnextgenx.base.modules.credentials.models;

import java.util.List;

import lombok.Data;
import lombok.Builder;
import lombok.ToString;
import lombok.NoArgsConstructor;

@Data
@Builder
@ToString
public class AppxUser {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private List<String> roles;
}
