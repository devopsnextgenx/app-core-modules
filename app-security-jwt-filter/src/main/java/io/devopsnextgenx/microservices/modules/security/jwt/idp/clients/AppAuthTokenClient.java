package io.devopsnextgenx.microservices.modules.security.jwt.idp.clients;

import com.nimbusds.oauth2.sdk.*;
import com.nimbusds.oauth2.sdk.auth.ClientAuthenticationMethod;
import com.nimbusds.oauth2.sdk.auth.ClientSecretBasic;
import com.nimbusds.oauth2.sdk.auth.Secret;
import com.nimbusds.oauth2.sdk.http.HTTPRequest;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.nimbusds.oauth2.sdk.id.ClientID;
import com.nimbusds.oauth2.sdk.id.Issuer;
import com.nimbusds.oauth2.sdk.token.AccessToken;
import com.nimbusds.openid.connect.sdk.OIDCTokenResponse;
import com.nimbusds.openid.connect.sdk.OIDCTokenResponseParser;
import com.nimbusds.openid.connect.sdk.op.OIDCProviderConfigurationRequest;
import com.nimbusds.openid.connect.sdk.op.OIDCProviderMetadata;
import com.nimbusds.openid.connect.sdk.token.OIDCTokens;
import io.devopsnextgenx.microservices.modules.exception.AppException;
import io.devopsnextgenx.microservices.modules.security.jwt.config.OAuthConfig;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.net.URI;

@Slf4j
@Data
public class AppAuthTokenClient {

    private final OAuthConfig authConfig;
    private final ClientSecretBasic clientSecretBasic;
    private final OIDCProviderMetadata providerMetadata;

    @SneakyThrows
    public AppAuthTokenClient(OAuthConfig authConfig) {
        this.authConfig = authConfig;
        clientSecretBasic = new ClientSecretBasic(new ClientID(authConfig.getClientId()), new Secret(authConfig.getClientSecret()));
        OIDCProviderConfigurationRequest providerConfigRequest = new OIDCProviderConfigurationRequest(new Issuer(authConfig.getDomain()));

        HTTPResponse providerConfigHttpResponse = providerConfigRequest.toHTTPRequest().send();
        providerMetadata = OIDCProviderMetadata.parse(providerConfigHttpResponse.getContentAsJSONObject());

        Assert.notNull(providerMetadata.getTokenEndpointURI(), String.format("Auth domain '%s' has no 'token' end point ", authConfig.getDomain()));
        Assert.isTrue(providerMetadata.getTokenEndpointAuthMethods().contains(ClientAuthenticationMethod.CLIENT_SECRET_JWT), String.format("Auth domain '%s' token end point does not supports '%s' ", authConfig.getDomain(), ClientAuthenticationMethod.CLIENT_SECRET_JWT));
    }

    /**
     * Exchange grant code with OIDC tokens
     *
     * @param code        the grant code that was issued in the login process
     * @param redirectUrl the redirect URL that was used in the login process.
     * @return a new pair of OIDCTokens
     */
    @SneakyThrows
    public OIDCTokens exchangeCode(String code, String redirectUrl) {
        AuthorizationGrant authGrant = new AuthorizationCodeGrant(new AuthorizationCode(code), new URI(redirectUrl));
        TokenRequest tokenRequest = new TokenRequest(providerMetadata.getTokenEndpointURI(), clientSecretBasic, authGrant);

        HTTPResponse response = tokenRequest.toHTTPRequest().send();
        if (response.getStatusCode() != 200) {
            throw new AppException(AppException.ERROR_CODE.ACCESS_DENIED, "Failed to exchange grant code. Reason '%s'", response.getContent());
        }

        TokenResponse tokenResponse = OIDCTokenResponseParser.parse(response);
        OIDCTokenResponse oidcTokenResponse = (OIDCTokenResponse) tokenResponse;
        return oidcTokenResponse.getOIDCTokens();
    }


    /**
     * Renew an exiting token of logged in user.
     *
     * @param refreshToken the users refresh token from the session
     * @return a new set of valid token
     */
    @SneakyThrows
    public OIDCTokens renewToken(String refreshToken) {

        // Get new access token as part of the same authenticated session
        final HTTPRequest httpRequest = new HTTPRequest(HTTPRequest.Method.POST, providerMetadata.getTokenEndpointURI());
        httpRequest.setHeader("Authorization", clientSecretBasic.toHTTPAuthorizationHeader());
        httpRequest.setHeader("Content-Type", "application/x-www-form-urlencoded");
        httpRequest.setQuery("grant_type=refresh_token&refresh_token=" + refreshToken);

        HTTPResponse httpResponse = httpRequest.send();
        if (httpResponse.getStatusCode() != 200) {
            throw new AppException(AppException.ERROR_CODE.ACCESS_DENIED, "Failed to renew token. Reason '%s'", httpResponse.getContent());
        }

        TokenResponse tokenResponse = OIDCTokenResponseParser.parse(httpResponse);
        OIDCTokenResponse oidcTokenResponse = (OIDCTokenResponse) tokenResponse;
        return oidcTokenResponse.getOIDCTokens();
    }

    /**
     * Provides basic access_key token that we currently use in machine to machine authentication flows.
     *
     * @return a new access key token
     */
    @SneakyThrows
    public AccessToken clientCredentials() {
        log.info("Issuing App access token (ServerToServer)");
        final HTTPRequest httpRequest = new HTTPRequest(HTTPRequest.Method.POST, providerMetadata.getTokenEndpointURI());
        httpRequest.setHeader("Authorization", clientSecretBasic.toHTTPAuthorizationHeader());
        httpRequest.setHeader("Content-Type", "application/x-www-form-urlencoded");
        httpRequest.setQuery("grant_type=client_credentials&scope=account profile idp_account read:apikey auth.role auth. openid");

        HTTPResponse httpResponse = httpRequest.send();
        if (httpResponse.getStatusCode() != 200) {
            throw new AppException(AppException.ERROR_CODE.ACCESS_DENIED, "Failed to issue access token. Reason '%s'", httpResponse.getContent());
        }

        TokenResponse tokenResponse = OIDCTokenResponseParser.parse(httpResponse);
        return ((AccessTokenResponse) tokenResponse).getTokens().getAccessToken();
    }
}
