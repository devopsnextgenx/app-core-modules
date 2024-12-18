package io.devopsnextgenx.microservices.modules.security.jwt.facades;

import io.devopsnextgenx.microservices.modules.security.jwt.config.AuthProviderType;
import io.devopsnextgenx.microservices.modules.security.jwt.config.OAuthConfig;
import io.devopsnextgenx.microservices.modules.exception.AppException;
import io.devopsnextgenx.microservices.modules.models.User;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Auth0ClientFacadeTest {

    private Auth0ClientFacade auth0ClientFacade;

    @Before
    public void init() {
        OAuthConfig authConfig = new OAuthConfig();
        authConfig.setAuthType(AuthProviderType.AUTH0);
        authConfig.setDomain("https://devopsnextgenx.auth0.com/");
        authConfig.setClientId("AJn1FDdQtsuolZeU4MIK2O7NbXvjccKN");
        authConfig.setClientSecret("R40JLeatEu6mMUPAA25IyESgAjMmOUqE9LKI9gzWjejksoUJmoiZJaDzqublAFPw");
        authConfig.setAudience("AJn1FDdQtsuolZeU4MIK2O7NbXvjccKN");
        authConfig.setDbConnection("Username-Password-Authentication");

        auth0ClientFacade = new Auth0ClientFacade(authConfig);
    }

    @Test(expected = AppException.class)
    public void givenListOf51Email_whenGetUsersFullNames_shouldThrowAppException() {

        auth0ClientFacade.getUsersFullNames(Collections.nCopies(51, "test@example.com"));
    }

    @Test
    public void givenListOf50Emails_andOnly4EmailsAreKnown_whenGetUsersFullNames_shouldTReturnKnownUsersOnly() {

        List<String> emailList = IntStream.range(0, 45)
                .mapToObj(value -> String.format("test-%d@local.dev", value))
                .collect(Collectors.toList());

        emailList.add("amit.kshirsagar.13@gmail.com");
        emailList.add("poonam.kshirsagar.13@gmail.com");
        emailList.add("amogh.kshirsagar.13@gmail.com");

        Map<String, User> usersFullNames = auth0ClientFacade.getUsersFullNames(emailList);
        Assertions.assertThat(usersFullNames.values())
                .hasSize(4)
                .doesNotContainNull()
                .extracting(User::getFirstName)
                .contains("Amit", "Poonam", "Amogh");
    }
}
