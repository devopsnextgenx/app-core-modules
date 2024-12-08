package io.devopsnextgenx.microservices.modules.security.jwt.cache;

import com.auth0.jwt.JWTVerifier;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import io.devopsnextgenx.microservices.modules.exception.AppException;
import io.devopsnextgenx.microservices.modules.security.jwt.config.AppConfig;
import io.devopsnextgenx.microservices.modules.security.jwt.helpers.JwtVerifierHelper;
import io.devopsnextgenx.microservices.modules.security.jwt.config.AuthProviderType;
import io.devopsnextgenx.microservices.modules.security.jwt.config.OAuthConfig;
import io.devopsnextgenx.microservices.modules.security.jwt.dto.UserClientIdResponse;
import io.devopsnextgenx.microservices.modules.security.jwt.helpers.OAuthLoginHelper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Data
public class JWTVerifierCache {

    private final String APP_CLIENT_URL = "/api/app/client/";
    private final LoadingCache<String, JWTVerifier> jwtVerifierCache;

    public JWTVerifierCache(OAuthLoginHelper oAuthLoginHelper, RestTemplate restTemplate, JwtVerifierHelper jwtVerifierHelper, AppConfig appConfig, String userServiceHost) {
        jwtVerifierCache = initCache(oAuthLoginHelper, restTemplate, jwtVerifierHelper, appConfig, userServiceHost);
    }

    private LoadingCache<String, JWTVerifier> initCache(OAuthLoginHelper oAuthLoginHelper, RestTemplate restTemplate, JwtVerifierHelper jwtVerifierHelper, AppConfig appConfig, String userServiceHost) {
        return CacheBuilder.newBuilder().build(new CacheLoader<String, JWTVerifier>() {

            @Override
            public JWTVerifier load(String clientID) {

                String serverToServerToken = oAuthLoginHelper.getServerToServerToken();

                verifyClientRegistered(clientID, serverToServerToken, userServiceHost, restTemplate, appConfig);

                OAuthConfig oAuthConfig = new OAuthConfig();
                oAuthConfig.setClientId(clientID);
                oAuthConfig.setDomain(appConfig.getDomain());
                oAuthConfig.setAuthType(AuthProviderType.APP);

                log.info("Creating JWT validator for domain: '{}' and ClientID '{}'.", appConfig.getDomain(), clientID);
                return jwtVerifierHelper.initJWTVerifier(oAuthConfig);
            }
        });
    }

    /**
     * Verifies that the auth configuration presented exists in User service
     *
     * @param clientID
     * @param serverToServerToken
     * @param userServiceHost
     * @param restTemplate
     * @param appConfig
     */
    private void verifyClientRegistered(String clientID, String serverToServerToken, String userServiceHost, RestTemplate restTemplate, AppConfig appConfig) {
        final String loadClientsUrl = userServiceHost + APP_CLIENT_URL + clientID;
        ResponseEntity<UserClientIdResponse> response = restTemplate.exchange(loadClientsUrl, HttpMethod.GET, getServiceApiHeaders(serverToServerToken, appConfig.getServerToServerUser()), UserClientIdResponse.class);
        UserClientIdResponse clientExist = response.getBody();

        if (!clientExist.isClientExist()) {
            throw new AppException(AppException.ERROR_CODE.ACCESS_DENIED, "Client id not found for: %s", clientID);
        }
    }

    private HttpEntity getServiceApiHeaders(String token, String serverToServerUser) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        headers.set("username", serverToServerUser);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(null, headers);
    }

}
