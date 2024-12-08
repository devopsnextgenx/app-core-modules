package io.devopsnextgenx.microservices.modules.security.jwt.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OAuthConfig {

    @JsonProperty(required = true)
    private String domain;

    @JsonProperty(required = true)
    private String clientId;

    @JsonProperty(required = true)
    private String clientSecret;

    @JsonProperty(required = true)
    private AuthProviderType authType;

    private String audience; // exists only in Auth0 and is different from clientID only for V2 access token
    private String redirectUrl;
    private String dbConnection;
    private String inviteUserEP;
    private int expirationSec = 43200; //12h in sec
    private int notBeforeSec = 2;
    private int issuedAtSec = 2;
}
