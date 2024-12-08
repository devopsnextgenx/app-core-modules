package io.devopsnextgenx.microservices.modules.security.jwt.helpers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import io.devopsnextgenx.microservices.modules.security.jwt.cache.AppRSAKeyProviderCache;
import io.devopsnextgenx.microservices.modules.security.jwt.config.OAuthConfig;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class JwtVerifierHelper {

    public JWTVerifier initJWTVerifier(OAuthConfig authConfig) {
        String audience = Optional.ofNullable(authConfig.getAudience())
                .orElseGet(authConfig::getClientId);
        return JWT.require(Algorithm.RSA256(new AppRSAKeyProviderCache(authConfig)))
                .withIssuer(authConfig.getDomain())
                .withAudience(audience)
                .acceptNotBefore(authConfig.getNotBeforeSec())
                .acceptIssuedAt(authConfig.getIssuedAtSec())
                .acceptExpiresAt(authConfig.getExpirationSec())
                .build();
    }
}
