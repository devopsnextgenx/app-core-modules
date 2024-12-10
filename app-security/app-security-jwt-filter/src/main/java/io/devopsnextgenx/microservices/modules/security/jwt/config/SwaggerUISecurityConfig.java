package io.devopsnextgenx.microservices.modules.security.jwt.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SwaggerUISecurityConfig {
    private String swaggerUser;
    private String swaggerAdmin;
    private String swaggerUserPassword;
    private String swaggerAdminPassword;
}
