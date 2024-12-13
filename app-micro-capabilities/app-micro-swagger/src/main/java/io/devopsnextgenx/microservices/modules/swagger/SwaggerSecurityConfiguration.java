package io.devopsnextgenx.microservices.modules.swagger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableConfigurationProperties
@ConditionalOnProperty(value = "app.modules.swagger.enabled", havingValue = "true", matchIfMissing = true)
public class SwaggerSecurityConfiguration {
    @Value("${app.modules.swagger.title:Micro-Service}")
    private String title;
    @Value("${app.modules.swagger.description:Micro-Service API Description}")
    private String description;
    @Value("${app.modules.swagger.version:1.0.0}")
    private String version;

    final String securityBasicSchemeName = "basicAuth";
    final String securityJwtSchemeName = "JWT";
    final String keyName = "Authorization";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title(title)
                .version(version)
                .description(description))
                .addSecurityItem(
                    new SecurityRequirement()
                        .addList(securityBasicSchemeName, securityJwtSchemeName))
                .components(
                    new Components()
                        .addSecuritySchemes(securityBasicSchemeName, 
                            new SecurityScheme()
                            .name(keyName)
                            .type(SecurityScheme.Type.HTTP)
                            .scheme("basic"))
                        .addSecuritySchemes(securityJwtSchemeName,
                            new SecurityScheme()
                                .name(keyName)
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .description("Plain JWT Token")));
    }
}
