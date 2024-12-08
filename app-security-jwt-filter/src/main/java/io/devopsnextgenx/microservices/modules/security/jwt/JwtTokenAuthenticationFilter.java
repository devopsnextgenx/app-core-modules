package io.devopsnextgenx.microservices.modules.security.jwt;


import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.devopsnextgenx.microservices.modules.access.model.AccessData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {

    public final static String ACCESS_DATA_HEADER = "access-data";
    public final static String AUTHORIZATION_HEADER = "Authorization";
    public final static String USERNAME_HEADER = "username";
    public final static String TEST_HEADER = "test";

    // ID tokens claims
    public static final String CLAIM_EMAIL = "email";
    public static final String CLAIM_USER_ARRAY = "https://devopsnextgenx.auth0.com/user";
    public static final String CLAIM_FEDERATED = "is_federated";
    public static final String CLAIM_ORG_CODE = "org_code";

    private TokenValidator tokenValidator;

    public JwtTokenAuthenticationFilter(TokenValidator tokenValidator) {
        this.tokenValidator = tokenValidator;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        String rawToken = request.getHeader(AUTHORIZATION_HEADER);
        log.debug("Raw '{}' header token [{}]", AUTHORIZATION_HEADER, rawToken);

        /**
         * for non-authenticated request (e.g /health), The request will still need to path through the filter chain
         * just without having any authentication on the context.
         * The decision if its ok not to have authentication is configured in {@link SecurityTokenConfig.ApiJwtSecurityConfig}
         */
        if (rawToken != null && rawToken.length() > 10) { // some quick & basic validation without iteration on the chars

            try {
                DecodedJWT jwt = JWT.decode(rawToken);
                tokenValidator.validateToken(jwt);

                String headerUsername = request.getHeader(USERNAME_HEADER);
                AccessData accessData = AccessData.fromString(request.getHeader(ACCESS_DATA_HEADER));

                Map<String, Claim> claims = jwt.getClaims();

                if (claims != null && claims.size() > 0) {

                    // Get the username from claim, if not present it is server to server token and get the username from the header
                    String username = Optional.ofNullable(claims.get(CLAIM_EMAIL))
                            .map(Claim::asString)
                            .filter(StringUtils::isNotEmpty)
                            .orElse(headerUsername);

                    // 5. Create auth object
                    // UsernamePasswordAuthenticationToken: A built-in object, used by spring to represent the current authenticated / being authenticated user.
                    // It needs a list of authorities, which has type of GrantedAuthority interface, where SimpleGrantedAuthority is an implementation of that interface

                    AccessDataAuthenticationToken auth = new AccessDataAuthenticationToken(username,
                            rawToken,
                            null,
                            accessData,
                            Optional.ofNullable(request.getHeader(TEST_HEADER))
                                    .map(Boolean::parseBoolean)
                                    .orElse(false),
                            Optional.ofNullable(jwt.getClaim(CLAIM_USER_ARRAY))
                                    .map(Claim::asMap)
                                    .map(userArrayClaims -> userArrayClaims.get(CLAIM_FEDERATED))
                                    .map(Boolean.class::cast)
                                    .orElse(false));

                    // 6. Authenticate the user
                    // Now, user is authenticated
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }

            } catch (Throwable e) {

                log.error("Token validation failure.", e);
                // In case of failure. Make sure it's clear; so guarantee user won't be authenticated
                SecurityContextHolder.clearContext();
            }
        }
        // go to the next filter in the filter chain
        chain.doFilter(request, response);
    }

}