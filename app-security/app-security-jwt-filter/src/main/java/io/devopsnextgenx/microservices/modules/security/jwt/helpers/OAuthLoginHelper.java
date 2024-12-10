package io.devopsnextgenx.microservices.modules.security.jwt.helpers;

import io.devopsnextgenx.microservices.modules.security.jwt.config.AuthProviderType;
import io.devopsnextgenx.microservices.modules.security.jwt.config.OAuthApplicationsConfig;
import io.devopsnextgenx.microservices.modules.security.jwt.facades.AuthClientFacade;
import io.devopsnextgenx.microservices.modules.security.jwt.facades.AuthClientFacadeFactory;
import io.devopsnextgenx.microservices.modules.security.jwt.facades.TokenBearer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Provides facilities to acquire tokens for communicating to and between services.
 */
@Slf4j
public class OAuthLoginHelper {

    private final AuthClientFacade mainAuthClient;

    public OAuthLoginHelper(OAuthApplicationsConfig oAuthApplicationsConfig) {
        AuthProviderType defaultAuthType = oAuthApplicationsConfig.getDefaultAuthType();
        log.info("Using '{}' as default Identity Provider client", defaultAuthType);

        // Client Credentials token (server to server) can be issued only by Machine2Machine tenant in Auth0 (our auth0-v2 tenant)
        String tenantName = defaultAuthType == AuthProviderType.AUTH0 ? AuthProviderType.AUTH0.getNodeName() + "-v2" : defaultAuthType.getNodeName();
        mainAuthClient = AuthClientFacadeFactory.createClient(tenantName, oAuthApplicationsConfig.getApplications().get(tenantName));
    }

    /**
     * Provides an {@link TokenBearer#getIdToken() id_token} if the {@link AuthClientFacade} used
     * supports user/password login operation.
     * Currently only {@link AuthProviderType#SELFSIGNED} is supported
     *
     * @param subject the identity of the user - usually email
     * @param <T>     String, Integer, Boolean or Map of claim name and its value
     * @return A signed JWT of the respected subject.
     */
    public <T> String login(String subject) {
        return login(subject, (Pair<String, Object>[]) null);
    }

    /**
     * Provides an {@link TokenBearer#getIdToken() id_token} if the {@link AuthClientFacade} used
     * supports user/password login operation.
     * Currently only {@link AuthProviderType#SELFSIGNED} is supported
     *
     * @param subject         the identity of the user - usually email
     * @param additionalClaim additional set of claims that should exist on the signed token
     * @param <T>             String, Integer, Boolean or Map of claim name and its value
     * @return A signed JWT of the respected subject.
     */
    @SuppressWarnings("unchecked")
    public String login(String subject, Pair<String, Object>... additionalClaim) {
        return mainAuthClient.login(subject, additionalClaim).getIdToken();
    }

    /**
     * Provides Server to Server token issued by the default IDP used {@link OAuthApplicationsConfig#getDefaultAuthType()}
     *
     * @return a signed server to server token by the external IDP.
     */
    public String getServerToServerToken() {
        return mainAuthClient.getServer2ServerToken().getAccessToken();
    }
}


