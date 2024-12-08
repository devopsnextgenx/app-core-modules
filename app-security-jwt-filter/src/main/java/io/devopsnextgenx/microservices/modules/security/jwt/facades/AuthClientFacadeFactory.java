package io.devopsnextgenx.microservices.modules.security.jwt.facades;

import io.devopsnextgenx.microservices.modules.exception.AppException;
import io.devopsnextgenx.microservices.modules.security.jwt.config.AuthProviderType;
import io.devopsnextgenx.microservices.modules.security.jwt.config.OAuthConfig;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class AuthClientFacadeFactory {

    private final static Map<AuthProviderType, Function<OAuthConfig, io.devopsnextgenx.microservices.modules.security.jwt.facades.AuthClientFacade>> clients = new HashMap<>();

    static {
        clients.put(AuthProviderType.AUTH0, io.devopsnextgenx.microservices.modules.security.jwt.facades.Auth0ClientFacade::new);
        clients.put(AuthProviderType.APP, AppClientFacade::new);
        clients.put(AuthProviderType.SELFSIGNED, io.devopsnextgenx.microservices.modules.security.jwt.facades.SelfSignedClientFacade::new);
    }

    public static io.devopsnextgenx.microservices.modules.security.jwt.facades.AuthClientFacade createClient(String tenantName, OAuthConfig authConfig) {

        Assert.notNull(authConfig, String.format("OAuthConfig is missing for tenant: '%s'.", tenantName));
        AuthProviderType authProviderType = Optional.ofNullable(authConfig.getAuthType())
                .orElseThrow(() -> new AppException(AppException.ERROR_CODE.OTHER, "Failed to initialize AuthClient for tenant: '%s'. missing Auth Provider type.", tenantName));

        return Optional.ofNullable(clients.get(authProviderType).apply(authConfig))
                .orElseThrow(() -> new AppException(AppException.ERROR_CODE.UNPROCESSABLE_ENTITY, "Failed to initialize AuthClient for tenant: '%s'. Not supported auth client was found for Auth Provider type: '%s'", tenantName, authConfig.getAuthType()));
    }
}
