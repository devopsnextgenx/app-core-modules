package io.devopsnextgenx.microservices.modules.security.jwt.facades;

import com.google.common.base.Supplier;
import com.nimbusds.openid.connect.sdk.token.OIDCTokens;
import io.devopsnextgenx.microservices.modules.security.jwt.config.AuthProviderType;
import io.devopsnextgenx.microservices.modules.security.jwt.config.OAuthConfig;
import io.devopsnextgenx.microservices.modules.security.jwt.idp.clients.AppAuthTokenClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class AppClientFacade extends AbstractAuthClientFacade {

    public static final String APP_HEALTHCHECK_ENDPOINT = "/api/service/ping";

    private final RestTemplate restTemplate = new RestTemplate();
    private final AppAuthTokenClient client;
    private Supplier<TokenBearer> server2serverTokenCache;

    public AppClientFacade(OAuthConfig authConfig) {
        super(authConfig);
        this.client = new AppAuthTokenClient(authConfig);
    }

    @Override
    public TokenBearer renewToken(String refreshToken) {

        OIDCTokens oidcTokens = client.renewToken(refreshToken);
        return convert2TokenBearer(oidcTokens);
    }

    @Override
    public TokenBearer exchangeCode(String grantCode, String redirectUri) {

        OIDCTokens oidcTokens = client.exchangeCode(grantCode, redirectUri);
        return convert2TokenBearer(oidcTokens);
    }

    @Override
    public AuthProviderType getAuthProviderType() {
        return AuthProviderType.APP;
    }

    @Override
    public boolean IDPHealthCheck() {
        log.debug("Start Health Check for App");
        ResponseEntity<Void> response = restTemplate.getForEntity(authConfig.getDomain().concat(APP_HEALTHCHECK_ENDPOINT), Void.class);
        return response.getStatusCode() == HttpStatus.OK;
    }

    @Override
    protected TokenBearer createAccessToken() {
        return TokenBearer.builder()
                .accessToken(client.clientCredentials().getValue())
                .build();
    }

    private TokenBearer convert2TokenBearer(OIDCTokens oidcTokens) {
        return TokenBearer.builder()
                .idToken(oidcTokens.getIDTokenString())
                .accessToken(oidcTokens.getAccessToken().getValue())
                .build();
    }
}

