package io.devopsnextgenx.microservices.modules.security.jwt.facades;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.impl.PublicClaims;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.collect.ImmutableMap;
import io.devopsnextgenx.microservices.modules.exception.AppException;
import io.devopsnextgenx.microservices.modules.model.User;
import io.devopsnextgenx.microservices.modules.security.jwt.config.AuthProviderType;
import io.devopsnextgenx.microservices.modules.security.jwt.config.OAuthConfig;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.util.Assert;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static io.devopsnextgenx.microservices.modules.security.jwt.JwtTokenAuthenticationFilter.*;

@Slf4j
public class SelfSignedClientFacade extends AbstractAuthClientFacade {


    public SelfSignedClientFacade(OAuthConfig authConfig) {
        super(authConfig);
        // in order to use one self signed tenant that will work for both AppAuthFilter, UserIdentityAuthFilter
        // the domains of should contain 'devopsnextgenx' to pass AppAuthFilter
        Assert.isTrue(authConfig.getDomain().contains("devopsnextgenx"), "SelfSigned token domain should contains 'appauth'");
    }

    @Override
    public TokenBearer login(String subject) {
        return login(subject, null);
    }

    @SneakyThrows
    @Override
    public TokenBearer login(String subject, Pair<String, Object>... additionalClaims) {
        OAuthConfig authProperties = Optional.ofNullable(authConfig)
                .orElseThrow(() -> new AppException(AppException.ERROR_CODE.OTHER, "Missing security configuration entry '%s' for SelfSigned JWT", AuthProviderType.SELFSIGNED.getNodeName()));

        // use the AuthClientId as secret for testing
        Algorithm algorithm = Algorithm.HMAC256(authProperties.getClientId());

        Date expiration = getClaim(PublicClaims.EXPIRES_AT, Date.class, additionalClaims)
                .orElse(Date.from(Instant.now().plusMillis(TimeUnit.MILLISECONDS.convert(24, TimeUnit.HOURS))));

        JWTCreator.Builder jwtBuilder = JWT.create()
                .withIssuer(authProperties.getDomain())
                .withSubject(subject)
                .withClaim("email", subject)
                .withAudience(authProperties.getClientId())
                .withIssuedAt(new Date())
                .withExpiresAt(expiration)
                .withAudience(authProperties.getClientId());

        // https://stackoverflow.com/questions/30308839/why-does-passing-null-to-varargs-give-length-1/30308865
        if (additionalClaims != null && additionalClaims[0] != null) {
            Stream.of(additionalClaims).forEach(additionalClaim -> {
                switch (additionalClaim.getValue().getClass().getSimpleName()) {
                    case "Integer":
                        jwtBuilder.withClaim(additionalClaim.getKey(), (Integer) additionalClaim.getValue());
                        break;
                    case "String":
                        jwtBuilder.withClaim(additionalClaim.getKey(), (String) additionalClaim.getValue());
                        break;
                    case "Boolean":
                        jwtBuilder.withClaim(additionalClaim.getKey(), (Boolean) additionalClaim.getValue());
                        break;
                    default:
                        if (additionalClaim.getValue() instanceof Map) {
                            jwtBuilder.withClaim(additionalClaim.getKey(), (Map<String, ?>) additionalClaim.getValue());
                        }
                }
            });
        }
        String selfSignedToken = jwtBuilder.sign(algorithm);
        return TokenBearer.builder()
                .idToken(selfSignedToken)
                .accessToken(selfSignedToken)
                .build();
    }

    @Override
    public TokenBearer renewToken(String refreshToken) {
        DecodedJWT token = JWT.decode(refreshToken);
        return login(token.getSubject());
    }

    @Override
    public TokenBearer exchangeCode(String grantCode, String redirectUri) {
        Pair<String, Object> federatedClaim = null;

        if (StringUtils.containsIgnoreCase(redirectUri, "federated")) {
            federatedClaim = Pair.of(CLAIM_USER_ARRAY, ImmutableMap.of(CLAIM_FEDERATED, true, CLAIM_ORG_CODE, grantCode));
        }
        return login(grantCode, federatedClaim);
    }

    @Override
    public Map<String, User> getUsersFullNames(List<String> usersEmails) {
        Map<String, User> responseTest = new HashMap<>();
        usersEmails.forEach(userEmail -> {
            responseTest.put(userEmail, new User(userEmail, userEmail));
        });
        return responseTest;
    }

    @Override
    public void inviteUserByEmail(String userEmail, EmailTemplate emailTemplate, String loginURL) {
        log.info("Skipping invite user '{}'. SelfSigned is not supporting sending emails.", userEmail);
    }

    @Override
    protected TokenBearer createAccessToken() {
        return login(null);
    }

    @Override
    public AuthProviderType getAuthProviderType() {
        return AuthProviderType.SELFSIGNED;
    }

    @Override
    public boolean IDPHealthCheck() {
        return true;
    }

    private <T> Optional<T> getClaim(String claim, Class<T> type, Pair... additionalClaims) {
        if (additionalClaims != null && additionalClaims[0] != null) {
            return Stream.of(additionalClaims).filter(claimPair -> claimPair.getKey().equals(claim))
                    .map(Pair::getValue)
                    .map(type::cast)
                    .findFirst();
        }
        return Optional.empty();
    }
}
