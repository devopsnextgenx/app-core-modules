package io.devopsnextgenx.demo.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Demo REST API", version = "1.0",
        description = "Demo REST API description...",
        contact = @Contact(name = "amit.kshirsagar.13@gmail.com"))
)
public class OpenApiConfig {
    
}
