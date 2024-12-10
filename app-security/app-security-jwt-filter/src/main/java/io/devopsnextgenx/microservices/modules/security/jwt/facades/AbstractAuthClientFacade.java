package io.devopsnextgenx.microservices.modules.security.jwt.facades;

import com.google.common.base.Suppliers;
import io.devopsnextgenx.microservices.modules.security.jwt.config.OAuthConfig;
import org.springframework.util.Assert;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public abstract class AbstractAuthClientFacade implements AuthClientFacade {

    protected final OAuthConfig authConfig;
    protected final Supplier<TokenBearer> server2serverTokenCache;

    public AbstractAuthClientFacade(OAuthConfig authConfig) {
        this.authConfig = authConfig;
        Assert.notNull(getAuthProviderType(), "No authentication provider type was set for this AuthClientFacade");
        Assert.isTrue(getAuthProviderType() == authConfig.getAuthType(), "Illegal auth type provided: " + authConfig.getAuthType());
        server2serverTokenCache = Suppliers.memoizeWithExpiration(this::createAccessToken, authConfig.getExpirationSec() - 60, TimeUnit.SECONDS);
    }

    protected abstract TokenBearer createAccessToken();

    @Override
    public TokenBearer getServer2ServerToken() {
        return server2serverTokenCache.get();
    }
}
