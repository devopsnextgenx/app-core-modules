package io.devopsnextgenx.base.modules.config.models;

import lombok.Data;

@Data
public class AppxApplication {
    private String authType;
    private String domain;
    private String clientId;
    private String clientSecret;
    private String audience;
    private String redirectUrl;
    private String dbConnection;
}
