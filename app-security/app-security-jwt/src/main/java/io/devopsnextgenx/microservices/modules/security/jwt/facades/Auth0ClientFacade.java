package io.devopsnextgenx.microservices.modules.security.jwt.facades;

import com.auth0.client.auth.AuthAPI;
import com.auth0.client.mgmt.ManagementAPI;
import com.auth0.client.mgmt.filter.UserFilter;
import com.auth0.json.auth.TokenHolder;
import com.auth0.json.mgmt.users.UsersPage;
import com.auth0.net.AuthRequest;
import com.fasterxml.jackson.annotation.JsonInclude;

import io.devopsnextgenx.microservices.modules.exception.AppException;
import io.devopsnextgenx.microservices.modules.security.models.User;
import io.devopsnextgenx.microservices.modules.security.jwt.config.AuthProviderType;
import io.devopsnextgenx.microservices.modules.security.jwt.config.OAuthConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class Auth0ClientFacade extends AbstractAuthClientFacade {

    public static final String USER_METADATA_FIRST_NAME = "first_name";
    public static final String USER_METADATA_LAST_NAME = "last_name";
    public static final String USER_METADATA_USER_NAME = "user_name";
    public static final String USER_METADATA_USER_EMAIL = "user_email";
    public static final int AUTH0_FULL_NAMES_API__MAX_ALLOWED_ITEMS_PER_CALL = 50;
    public static final String AUTH0_HEALTHCHECK_ENDPOINT = "testall";

    private final AuthAPI client;
    private final RestTemplate restTemplate = new RestTemplate();

    public Auth0ClientFacade(OAuthConfig authConfig) {
        super(authConfig);
        this.client = new AuthAPI(authConfig.getDomain(), authConfig.getClientId(), authConfig.getClientSecret());
    }

    @Override
    @SneakyThrows
    public TokenBearer renewToken(String refreshToken) {
        TokenHolder tokenHolder = client.renewAuth(refreshToken)
                .execute();
        return convert2TokenBearer(tokenHolder);
    }

    @Override
    @SneakyThrows
    public TokenBearer exchangeCode(String grantCode, String redirectUri) {

        TokenHolder tokenHolder = client.exchangeCode(grantCode, redirectUri)
                .execute();
        return convert2TokenBearer(tokenHolder);
    }

    @Override
    @SneakyThrows
    public Map<String, User> getUsersFullNames(List<String> usersEmails) {
        if (usersEmails.size() > AUTH0_FULL_NAMES_API__MAX_ALLOWED_ITEMS_PER_CALL) {
            throw new AppException(AppException.ERROR_CODE.UNPROCESSABLE_ENTITY, "Auth0 full names API cannot get more than '%d' emails per request. Given: '%d'", AUTH0_FULL_NAMES_API__MAX_ALLOWED_ITEMS_PER_CALL, usersEmails.size());
        }
        if (usersEmails.isEmpty()) {
            return Collections.emptyMap();
        }
        log.info("Getting full names for user with emails: '{}'", usersEmails);
        String emailsFormatted = usersEmails.stream()
                .map(this::quotify)
                .collect(Collectors.joining(","));

        UserFilter userFilter = new UserFilter();
        userFilter.withQuery(String.format("identities.connection:%s AND email:(%s)", authConfig.getDbConnection(), emailsFormatted));

        ManagementAPI managementAPI = new ManagementAPI(authConfig.getDomain(), server2serverTokenCache.get().getAccessToken());
        UsersPage usersPage = managementAPI.users()
                .list(userFilter).execute();

        return usersPage.getItems().stream()
                .collect(Collectors.toMap(com.auth0.json.mgmt.users.User::getEmail, this::buildUserFromUserMetaData));
    }

    @Override
    public void inviteUserByEmail(String userEmail, EmailTemplate emailTemplate, String loginURL) {
        try {
            Assert.notNull(authConfig.getInviteUserEP(), "Missing invite user end point configuration 'inviteUserEP' in the provided auth0-v2 tenant");
            String apiToken = server2serverTokenCache.get().getAccessToken();
            String inviteUserEP = authConfig.getInviteUserEP() + "/v1/users/" + userEmail + "/invite";

            InviteUserBody body = new InviteUserBody(loginURL, emailTemplate.getAppkeyValue());

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(apiToken);
            headers.set(HttpHeaders.ACCEPT, "application/json");
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Object> httpEntity = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.exchange(inviteUserEP, HttpMethod.POST, httpEntity, String.class);
            log.info("User invitation email was sent to '{}'.", userEmail);

            if (HttpStatus.OK != response.getStatusCode()) {
                throw new AppException(AppException.ERROR_CODE.OTHER, response.getBody());
            }

        } catch (Exception e) {
            throw new AppException(AppException.ERROR_CODE.OTHER, "Failed to invite user: '%s' ", userEmail, e);
        }
    }

    @Override
    public AuthProviderType getAuthProviderType() {
        return AuthProviderType.AUTH0;
    }

    @Override
    public boolean IDPHealthCheck() {
        log.debug("Start Health Check for Auth0");

        ResponseEntity<String> response = restTemplate.getForEntity(authConfig.getDomain().concat(AUTH0_HEALTHCHECK_ENDPOINT), String.class);
        return response.getStatusCode() == HttpStatus.OK && response.getBody().equals("OK");
    }

    @Override
    @SneakyThrows
    protected TokenBearer createAccessToken() {
        AuthRequest authRequest = client.requestToken(Optional.ofNullable(authConfig.getAudience()).orElse(authConfig.getClientId()));
        TokenHolder holder = authRequest.execute();

        return TokenBearer.builder()
                .accessToken(holder.getAccessToken())
                .build();
    }

    private TokenBearer convert2TokenBearer(TokenHolder tokenHolder) {
        return TokenBearer.builder()
                .idToken(tokenHolder.getIdToken())
                .accessToken(tokenHolder.getAccessToken())
                .expiresIn(tokenHolder.getExpiresIn())
                .build();
    }

    private User buildUserFromUserMetaData(com.auth0.json.mgmt.users.User user) {
        return User.builder()
            .firstName(user.getUserMetadata().get(USER_METADATA_FIRST_NAME).toString())
            .lastName(user.getUserMetadata().get(USER_METADATA_LAST_NAME).toString())
            .userName(user.getUserMetadata().get(USER_METADATA_USER_NAME).toString())
            .email(user.getUserMetadata().get(USER_METADATA_USER_EMAIL).toString()).build();
    }

    private String quotify(String stirng) {
        return "\"" + stirng + "\"";
    }

    @Data
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private static class InviteUserBody {
        private final String tenantLink;
        private final String callingApplication;
    }
}
