package io.devopsnextgenx.microservices.modules.security.jwt.cache;

import com.auth0.jwk.GuavaCachedJwkProvider;
import com.auth0.jwk.JwkException;
import com.auth0.jwk.UrlJwkProvider;
import com.auth0.jwt.interfaces.RSAKeyProvider;
import io.devopsnextgenx.microservices.modules.exception.AppException;
import io.devopsnextgenx.microservices.modules.security.jwt.config.OAuthConfig;
import lombok.SneakyThrows;
import org.springframework.lang.NonNull;

import java.net.URL;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.concurrent.TimeUnit;

public class AppRSAKeyProviderCache implements RSAKeyProvider {
    private final String APP_JWKS_URI_SUFFIX = "certs";
    private final GuavaCachedJwkProvider jwkProvider;

    @SneakyThrows
    public AppRSAKeyProviderCache(@NonNull OAuthConfig authConfig) {

        UrlJwkProvider urlJwkProvider;

        switch (authConfig.getAuthType()) {
            case APP:
                urlJwkProvider = new UrlJwkProvider(new URL(authConfig.getDomain() + APP_JWKS_URI_SUFFIX));
                break;
            case AUTH0:
                urlJwkProvider = new UrlJwkProvider(JwkURLResolver.resolveDomain(authConfig.getDomain()));
                break;
            case SELFSIGNED:
                urlJwkProvider = null;
                break;
            default:
                throw new UnsupportedOperationException(String.format("No UrlJwkProvider was defined for Auth Provider Type: '%s'", authConfig.getAuthType()));
        }
        this.jwkProvider = new GuavaCachedJwkProvider(urlJwkProvider, 100, 24, TimeUnit.HOURS);
    }

    @Override
    public RSAPublicKey getPublicKeyById(String kid) {
        return (RSAPublicKey) requestPublicKey(kid);
    }

    @Override
    public RSAPrivateKey getPrivateKey() {
        return null;
    }

    @Override
    public String getPrivateKeyId() {
        return null;
    }

    private PublicKey requestPublicKey(String kid) {

        try {
            return jwkProvider.get(kid).getPublicKey(); //throws Exception when not found or can't get one
        } catch (JwkException e) {
            throw new AppException(AppException.ERROR_CODE.OTHER, "Could not locate public key for JWT with kid: '%s'", kid, e);
        }
    }
}
