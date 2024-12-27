package io.devopsnextgenx.base.modules.credentials.models;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class AppxApplication {
    private String authType;
    private String domain;
    private String clientId;
    private String clientSecret;
    private String audience;
    private String redirectUrl;
    private String dbConnection;
}
