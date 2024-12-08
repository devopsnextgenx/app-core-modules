package io.devopsnextgenx.microservices.modules.security.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;

/**
 * Responsible for JWT validation of a specific implementation provider
 */
public interface TokenValidator {

    /**
     * validate a given token, and if validation succeeded a map of claims will be returned
     * that are part of the JWT payload.
     *
     * @param token the decoded JWT before validation
     */
    void validateToken(DecodedJWT token);
}
