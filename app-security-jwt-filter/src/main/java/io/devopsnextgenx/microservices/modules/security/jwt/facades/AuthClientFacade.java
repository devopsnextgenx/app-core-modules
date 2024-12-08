package io.devopsnextgenx.microservices.modules.security.jwt.facades;

import io.devopsnextgenx.microservices.modules.model.User;
import io.devopsnextgenx.microservices.modules.security.jwt.config.AuthProviderType;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;

/**
 * A facade for creating Authentication client for some specific Identity Provider that will externalize
 * A uniform operations across all IDPs while implementation specifics are written in each concrete implementation.
 */
public interface AuthClientFacade {

    /**
     * Login a user with a given subject
     *
     * @param subject the JWT subject
     * @return a sign JWT token provided by the Auth IDP
     */
    default TokenBearer login(String subject) {
        throw new UnsupportedOperationException(String.format("login operation is not supported in '%s' Client ", getAuthProviderType()));
    }

    /**
     * Login a user with a given subject
     *
     * @param subject         the JWT subject
     * @param additionalClaim additional set of claims needed to be on the token
     * @param <T>             primitive claim value
     * @return a sign JWT token provided by the Auth IDP with additional claims
     */
    default TokenBearer login(String subject, @SuppressWarnings("unchecked") Pair<String, Object>... additionalClaim) {
        throw new UnsupportedOperationException(String.format("login operation is not supported in '%s' Client ", getAuthProviderType()));
    }

    /**
     * Issues a renewal request to invoke a new ID-Token from the underlying {@link AuthProviderType}
     *
     * @param refreshToken the original refresh token that was issued to the original ID-Token.
     * @return a renewed ID-Token.
     */
    TokenBearer renewToken(String refreshToken);

    /**
     * Exchange a grant code with a new set of id_token and access_token (Implicit Grant)
     * https://datatracker.ietf.org/doc/html/rfc6749#section-4.2
     *
     * @param grantCode   the grant code obtained in the redirect page
     * @param redirectUri the URL from which the the login page initiated
     * @return a {@link com.auth0.json.auth.TokenHolder} with a new set of id_token and access_token
     */
    TokenBearer exchangeCode(String grantCode, String redirectUri);

    /**
     * Call the Identity provider for retireving full names by user emails
     *
     * @param usersEmails a list of emails strings
     * @return a list of {@link User}s
     */
    default Map<String, User> getUsersFullNames(List<String> usersEmails) {
        throw new UnsupportedOperationException(String.format("User Full Names API is not supported in '%s' Client ", getAuthProviderType()));
    }


    /**
     * Send an invite email to a designated user's email address.
     *
     * @param userEmail     the email "to" address
     * @param emailTemplate what kind of invite email to send
     * @param loginURL      the login URL link to be embedded in the email request
     */
    default void inviteUserByEmail(String userEmail, EmailTemplate emailTemplate, String loginURL) {
        throw new UnsupportedOperationException(String.format("Invite User API is not supported in '%s' Client ", getAuthProviderType()));
    }

    /**
     * Provides basic access_key token that we currently use in machine to machine authentication flows.
     *
     * @return a new {@link TokenBearer} with only access key token
     */
    TokenBearer getServer2ServerToken();

    /**
     * @return The type of the current {@link AuthClientFacade}
     */
    AuthProviderType getAuthProviderType();

    /**
     * Invokes a health check for a designated IDP
     *
     * @return true if IDP is alive and working else false.
     */
    public boolean IDPHealthCheck();
}
