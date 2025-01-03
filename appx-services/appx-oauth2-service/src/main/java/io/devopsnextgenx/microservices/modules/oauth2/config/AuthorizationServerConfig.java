package io.devopsnextgenx.microservices.modules.oauth2.config;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.web.context.WebApplicationContext;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import io.devopsnextgenx.microservices.modules.access.model.AuthenticationFacade;
import io.devopsnextgenx.microservices.modules.oauth2.utils.JwtTokenProvider;
import io.devopsnextgenx.microservices.modules.oauth2.utils.UserCloner;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * <b>Overview:</b>
 * <p>
 * 
 * 
 * <pre>
 * &#64;projectName oauth-service
 * Creation date: Nov 4, 2023
 * &#64;author Amit Kshirsagar
 * &#64;version 1.0
 * &#64;since
 * 
 * <p><b>Modification History:</b><p>
 * 
 * 
 * </pre>
 */

@Slf4j
@Configuration
@EnableWebSecurity
public class AuthorizationServerConfig {

	@Autowired
	private JwtTokenProvider tokenProvider;

	@Bean
	public UserCloner userCloner() {
		return new UserCloner();
	}

	@Bean
	@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
	public AuthenticationFacade authenticationFacade() {
		return new AuthenticationFacade();
	}

	@Bean
	@Order(1)
	public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
		OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = OAuth2AuthorizationServerConfigurer
				.authorizationServer().oidc(Customizer.withDefaults());
		http.securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
				.with(authorizationServerConfigurer, Customizer.withDefaults())
				.authorizeHttpRequests((authorize) -> authorize.anyRequest().authenticated())
				.exceptionHandling((exceptions) -> exceptions.defaultAuthenticationEntryPointFor(
						new LoginUrlAuthenticationEntryPoint("/login"),
						new MediaTypeRequestMatcher(MediaType.TEXT_HTML)))
				// Accept access tokens for User Info and/or Client Registration
				.oauth2ResourceServer((resourceServer) -> resourceServer
						.jwt(Customizer.withDefaults()));

		return http.build();
	}

	@Bean
	@Order(2)
	public SecurityFilterChain loginOAuth2SecurityFilterChain(HttpSecurity http)
			throws Exception {
		http
				.securityMatcher("/form/login/**")
				.authorizeHttpRequests((authorize) -> authorize
						.anyRequest().authenticated())
				// Form login handles the redirect to the login page from the
				// authorization server filter chain
				.formLogin(Customizer.withDefaults());

		return http.build();
	}

	@Bean
	@Order(3)
	public SecurityFilterChain registerOAuth2SecurityFilterChain(HttpSecurity http)
			throws Exception {

		http
				.securityMatcher("/content/**")
				.authorizeHttpRequests(authorize -> authorize
						.requestMatchers("/content/public/**").permitAll() // Public resources
						.anyRequest().authenticated() // All other requests need authentication
				)
				.httpBasic(Customizer.withDefaults()) // Enable Basic Auth
				.csrf(csrf -> csrf.disable()); // Disable CSRF for simplicity

		return http.build();
	}

	@Bean
	@Order(4)
	public SecurityFilterChain oauth2FilterChain(HttpSecurity http) throws Exception {
		http
			.securityMatcher("/oauth2/**", "/login/**") 
			.csrf(csrf -> csrf.disable())
			.authorizeHttpRequests(auth -> auth
					.requestMatchers("/oauth2/**", "/login/**").permitAll()
					.anyRequest().authenticated())
			.oauth2Login().successHandler((request, response, authentication) -> {
                OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
                String token = tokenProvider.createToken(oauth2User);
                response.getWriter().write(token);
            });
		return http.build();
	}

	@Bean
	public JWKSource<SecurityContext> jwkSource() {
		KeyPair keyPair = generateRsaKey();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		RSAKey rsaKey = new RSAKey.Builder(publicKey)
				.privateKey(privateKey)
				.keyID(UUID.randomUUID().toString())
				.build();
		JWKSet jwkSet = new JWKSet(rsaKey);
		return new ImmutableJWKSet<>(jwkSet);
	}

	private static KeyPair generateRsaKey() {
		KeyPair keyPair;
		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(2048);
			keyPair = keyPairGenerator.generateKeyPair();
		} catch (Exception ex) {
			throw new IllegalStateException(ex);
		}
		return keyPair;
	}

	@Bean
	public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
		return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
	}

	@Bean
	public RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate) {
		String clientId = "react-app-client";
		RegisteredClient oidcClient = RegisteredClient.withId(UUID.randomUUID().toString())
				.clientId(clientId)
				.clientSecret("secret")
				.scope(OidcScopes.OPENID)
				.scope(OidcScopes.PROFILE)
				.redirectUri("http://127.0.0.1:8080/login/oauth2/code/react-app-client")
				.clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
				// .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
				.authorizationGrantTypes(grantType -> {
					grantType.add(AuthorizationGrantType.AUTHORIZATION_CODE);
					grantType.add(AuthorizationGrantType.REFRESH_TOKEN);
					grantType.add(AuthorizationGrantType.CLIENT_CREDENTIALS);
				})
				.postLogoutRedirectUri("http://127.0.0.1:8080/")
				// .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
				.clientSettings(ClientSettings.builder().requireProofKey(true).build())
				.build();

		RegisteredClientRepository registeredClientRepository = new JdbcRegisteredClientRepository(jdbcTemplate);

		if (registeredClientRepository.findByClientId(clientId) == null) {
			log.info("Save oidcClient");
			registeredClientRepository.save(oidcClient);
		}

		return registeredClientRepository;
	}

	@Bean
	public AuthorizationServerSettings authorizationServerSettings() {
		return AuthorizationServerSettings.builder()
				.issuer("http://auth-server.k8s.localtest.me:5000")
				.build();
	}
}