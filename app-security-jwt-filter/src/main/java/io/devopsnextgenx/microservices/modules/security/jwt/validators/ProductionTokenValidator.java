package io.devopsnextgenx.microservices.modules.security.jwt.validators;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.devopsnextgenx.microservices.modules.exception.AppException;
import io.devopsnextgenx.microservices.modules.security.jwt.TokenValidator;
import io.devopsnextgenx.microservices.modules.security.jwt.cache.JWTVerifierCache;
import io.devopsnextgenx.microservices.modules.security.jwt.config.OAuthApplicationsConfig;
import io.devopsnextgenx.microservices.modules.security.jwt.config.OAuthConfig;
import io.devopsnextgenx.microservices.modules.security.jwt.helpers.JwtVerifierHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class ProductionTokenValidator implements TokenValidator {

    private final JWTVerifierCache jwtVerifierCache;
    private final JwtVerifierHelper jwtVerifierHelper;

    // Working under the assumption that all auth0 applications has different audience property
    public ProductionTokenValidator(@NonNull OAuthApplicationsConfig oauthApplicationsConfig, JWTVerifierCache jwtVerifierCache, JwtVerifierHelper jwtVerifierHelper) {

        this.jwtVerifierCache = jwtVerifierCache;

        this.jwtVerifierHelper = jwtVerifierHelper;

        Map<String, JWTVerifier> clientIdVerifierMap = new HashMap<>();

        oauthApplicationsConfig.getApplications().forEach((tenant, authConfig) -> {
            validateAuthConfig(tenant, authConfig);
            if (clientIdVerifierMap.containsKey(authConfig.getClientId())) {
                throw new AppException(AppException.ERROR_CODE.OTHER, "Ambiguous clientID '%s'. OAuth applications must have a unique clientID.", authConfig.getClientId());
            }

            String verifierID = Optional.ofNullable(authConfig.getAudience())
                    .orElseGet(() -> authConfig.getClientId());

            clientIdVerifierMap.put(verifierID, initJWTVerifier(authConfig));
        });

        this.jwtVerifierCache.getJwtVerifierCache().putAll(clientIdVerifierMap);
    }

    protected JWTVerifier initJWTVerifier(OAuthConfig authConfig) {
        log.info("Creating JWT validator for domain: '{}' and ClientID '{}'.", authConfig.getDomain(), authConfig.getClientId());
        return jwtVerifierHelper.initJWTVerifier(authConfig);
    }

    @Override
    public void validateToken(DecodedJWT token) {
        Optional.ofNullable(jwtVerifierCache.getJwtVerifierCache().getUnchecked(token.getAudience().get(0)))
                .orElseThrow(() -> new AppException(AppException.ERROR_CODE.ACCESS_DENIED, "Could not find JWT token verifier for token with audience (clientID) : '%s'", token.getAudience()))
                .verify(token);
    }

    private void validateAuthConfig(String tenant, OAuthConfig authConfig) {

        String errorMessage = "Invalid OAuthConfig for tenant '%s', '%s' is missing.";
        Assert.notNull(authConfig.getDomain(), String.format(errorMessage, tenant, "domain"));
        Assert.notNull(authConfig.getClientId(), String.format(errorMessage, tenant, "clientId"));
    }
}
