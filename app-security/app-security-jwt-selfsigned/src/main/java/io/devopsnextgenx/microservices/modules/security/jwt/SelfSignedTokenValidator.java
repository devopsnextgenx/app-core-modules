package io.devopsnextgenx.microservices.modules.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import io.devopsnextgenx.microservices.modules.security.jwt.cache.JWTVerifierCache;
import io.devopsnextgenx.microservices.modules.security.jwt.config.OAuthApplicationsConfig;
import io.devopsnextgenx.microservices.modules.security.jwt.config.OAuthConfig;
import io.devopsnextgenx.microservices.modules.security.jwt.helpers.JwtVerifierHelper;
import io.devopsnextgenx.microservices.modules.security.jwt.validators.ProductionTokenValidator;
import lombok.extern.slf4j.Slf4j;

/**
 * Provides the ability to bypass external authentication providers (e.g. auth0.com)
 * with an internal JWT signing solution meant for <b>Testing Only</b>.
 */
@Slf4j
public class SelfSignedTokenValidator extends ProductionTokenValidator {


    public SelfSignedTokenValidator(OAuthApplicationsConfig oAuthApplicationsConfig, JWTVerifierCache jwtVerifierCache) {
        super(oAuthApplicationsConfig, jwtVerifierCache, new JwtVerifierHelper());
        log.warn("Using Self-Signed JWT validation. Safe for test mode only !");
    }

    @Override
    protected JWTVerifier initJWTVerifier(OAuthConfig authConfig) {
        log.info("Creating JWT validator for domain: '{}' and ClientID '{}'.", authConfig.getDomain(), authConfig.getClientId());
        return JWT.require(Algorithm.HMAC256(authConfig.getClientId()))
                .withIssuer(authConfig.getDomain())
                .withAudience(authConfig.getClientId())
                .acceptNotBefore(authConfig.getNotBeforeSec())
                .acceptIssuedAt(authConfig.getIssuedAtSec())
                .acceptExpiresAt(authConfig.getExpirationSec())
                .build();
    }

    @Override
    public void validateToken(DecodedJWT token) {
        // Optional.ofNullable(jwtVerifierCache.getJwtVerifierCache().getUnchecked(token.getAudience().get(0)))
        //         .orElseThrow(() -> new AppException(AppException.ERROR_CODE.ACCESS_DENIED, "Could not find JWT token verifier for token with audience (clientID) : '%s'", token.getAudience()))
        //         .verify(token);
    }
}
