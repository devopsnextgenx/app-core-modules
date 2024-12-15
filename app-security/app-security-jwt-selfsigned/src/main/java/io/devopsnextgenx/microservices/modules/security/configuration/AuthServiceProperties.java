package io.devopsnextgenx.microservices.modules.security.configuration;

import lombok.Data;

/**
 * AuthServiceProperties:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/4/2019
 */
@Data
public class AuthServiceProperties {
    private String apiV2Audience;
    private String callbackUrl;
    private String clientId;
    private String clientSecret;
    private String domain;
    private String issuer;
}