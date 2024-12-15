package io.devopsnextgenx.microservices.modules.security.jwt.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.devopsnextgenx.microservices.modules.exception.AppException;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OAuthApplicationsConfig {

    /**
     * This parameter determines the main authentication provider of the application.
     * Used to provide Server 2 Server token between services.
     * Also used to determine the grant/exchange/renew of token.
     */
    @JsonProperty(required = true)
    private AuthProviderType defaultAuthType;

    @JsonProperty(required = true)
    private Map<String, OAuthConfig> applications = new HashMap<>();

    public OAuthConfig getAppAuthConfig() {
        return Optional.ofNullable(applications.get(defaultAuthType.getNodeName()))
                .orElseThrow(() -> new AppException(AppException.ERROR_CODE.UNPROCESSABLE_ENTITY, "Default security tenant configuration is missing. Expected: '%s'", defaultAuthType.getNodeName()));
    }
}
