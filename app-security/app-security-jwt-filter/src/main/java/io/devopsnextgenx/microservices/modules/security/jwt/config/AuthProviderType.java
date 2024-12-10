package io.devopsnextgenx.microservices.modules.security.jwt.config;
/**
 * Holds the known IDPs that are used to authenticate and authorize users in the platform.
 */
public enum AuthProviderType {

    AUTH0("auth0"),
    APP("app"),
    SELFSIGNED("self-signed");

    private String nodeName;

    AuthProviderType(String mainAppConfig) {
        this.nodeName = mainAppConfig;
    }

    /**
     * The name of the {@link OAuthApplicationsConfig#getApplications() Application } in the security configuration
     * that holds the main IDP credentials for the current {@link AuthProviderType}
     *
     * @return name of the security application node.
     */
    public String getNodeName() {
        return nodeName;
    }

}
