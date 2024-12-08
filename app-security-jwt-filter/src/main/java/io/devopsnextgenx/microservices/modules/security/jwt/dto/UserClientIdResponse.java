package io.devopsnextgenx.microservices.modules.security.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserClientIdResponse {
    boolean isClientExist;
}
