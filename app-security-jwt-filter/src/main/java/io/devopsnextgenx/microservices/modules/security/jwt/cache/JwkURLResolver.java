package io.devopsnextgenx.microservices.modules.security.jwt.cache;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.UrlValidator;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * {@link com.auth0.jwk.UrlJwkProvider} can have a static domain for retrieving well known key providers for JWT.
 * or it can have a custom URL to fetch auth domains from there.
 * <p>
 * The purpose of this resolver is to create a {@link java.net.URL} for the {@link com.auth0.jwk.UrlJwkProvider#UrlJwkProvider(URL url)}
 * and to append "/.well-known/jwks.json" only in cases that no URL path was provided.
 * Read more at.
 * (@link https://auth0.com/docs/tokens/concepts/jwks)
 */
public class JwkURLResolver {

    static public final String WELL_KNOWN_JWKS_PATH = "/.well-known/jwks.json";


    public static URL resolveDomain(String domain) {

        if (StringUtils.isBlank(domain)) {
            throw new IllegalArgumentException("JWK domain cannot be empty");
        }

        if (!domain.startsWith("http")) {
            domain = "https://" + domain;
        }

        try {
            URI domainURI = new URI(domain);

            domainURI = addWellKnownJWKS(domainURI);
            validateURL(domainURI);
            return domainURI.toURL();

        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid auth domain URL", e);
        }
    }


    private static URI addWellKnownJWKS(URI domainURI) throws URISyntaxException {
        String urlPath = domainURI.normalize().getPath(); // get only the path part of the url
        if (urlPath == null || urlPath.isEmpty() || urlPath.equals("/")) {
            domainURI = new URI(domainURI.toString() + WELL_KNOWN_JWKS_PATH);
        }
        return domainURI.normalize();
    }

    private static void validateURL(URI domainURI) {
        UrlValidator validator = new UrlValidator(new String[]{"https"});
        if (!validator.isValid(domainURI.toString())) {
            throw new IllegalArgumentException("Invalid auth domain URL: " + domainURI);
        }
    }
}
