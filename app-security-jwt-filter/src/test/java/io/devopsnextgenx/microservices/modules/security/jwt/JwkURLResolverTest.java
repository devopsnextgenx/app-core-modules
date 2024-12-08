package io.devopsnextgenx.microservices.modules.security.jwt;

import io.devopsnextgenx.microservices.modules.security.jwt.cache.JwkURLResolver;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class JwkURLResolverTest {
    @Test
    public void noPathDomain_withDashedSuffix_shouldEndWith_WELL_KNOWN_JWKS_PATH() throws MalformedURLException {
        String domain = "https://devopsnextgenx.auth0.com/";
        assertThat(JwkURLResolver.resolveDomain(domain))
                .isEqualTo(new URL("https://devopsnextgenx.auth0.com/.well-known/jwks.json"));
    }

    @Test
    public void noPathDomain_shouldEndWith_WELL_KNOWN_JWKS_PATH() throws MalformedURLException {
        String domain = "https://devopsnextgenx.auth0.com";
        assertThat(JwkURLResolver.resolveDomain(domain))
                .isEqualTo(new URL("https://devopsnextgenx.auth0.com/.well-known/jwks.json"));
    }

    @Test
    public void noPathDomain_NoSchema_shouldEndWith_WELL_KNOWN_JWKS_PATH() throws MalformedURLException {
        String domain = "devopsnextgenx.auth0.com";
        assertThat(JwkURLResolver.resolveDomain(domain))
                .isEqualTo(new URL("https://devopsnextgenx.auth0.com/.well-known/jwks.json"));
    }

    @Test
    public void noPathDomain_WELL_KNOWN_JWKS_PATH_Suffix_shouldEndWith_OnlyOne_WELL_KNOWN_JWKS_PATH() throws MalformedURLException {
        String domain = "https://devopsnextgenx.auth0.com/" + JwkURLResolver.WELL_KNOWN_JWKS_PATH;
        assertThat(JwkURLResolver.resolveDomain(domain))
                .isEqualTo(new URL("https://devopsnextgenx.auth0.com/.well-known/jwks.json"));
    }

    @Test
    public void customPathDomainSuffix_shouldEndWith_customPath() throws MalformedURLException {
        String domain = "https://devopsnextgenx.auth0.com/.unknown/jwks.json";
        assertThat(JwkURLResolver.resolveDomain(domain))
                .isEqualTo(new URL("https://devopsnextgenx.auth0.com/.unknown/jwks.json"));
    }

    @Test
    public void customPathDomainSuffix_WithSchema_shouldEndWith_customPath() throws MalformedURLException {

        String domain = "devopsnextgenx.auth0.com/.unknown/jwks.json";

        assertThat(JwkURLResolver.resolveDomain(domain))
                .isEqualTo(new URL("https://devopsnextgenx.auth0.com/.unknown/jwks.json"));
    }

    @Test
    public void invalidDomainURL_shouldThrowIllegalArgumentException() {
        String domain = "devopsnextgenx*auth0*com";
        assertThatThrownBy(() -> JwkURLResolver.resolveDomain(domain))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid auth domain URL");
    }

    @Test
    public void missingDomainURL_shouldThrowIllegalArgumentException() {
        assertThatThrownBy(() -> JwkURLResolver.resolveDomain(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("JWK domain cannot be empty");
    }

    @Test
    public void nullDomainURL_shouldThrowIllegalArgumentException() {
        assertThatThrownBy(() -> JwkURLResolver.resolveDomain(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("JWK domain cannot be empty");
    }

}
