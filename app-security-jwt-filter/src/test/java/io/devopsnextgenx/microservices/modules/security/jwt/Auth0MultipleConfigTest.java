package io.devopsnextgenx.microservices.modules.security.jwt;

import io.devopsnextgenx.microservices.modules.security.jwt.config.OAuthApplicationsConfig;
import io.devopsnextgenx.microservices.modules.security.jwt.config.OAuthConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {SecurityTokenConfig.class, Auth0MultipleConfigTest.TestConfig.class})
@TestPropertySource(properties = {"spring.config.location = classpath:auth-config-test.yaml"})
public class Auth0MultipleConfigTest {

    /**
     * The external configuration `APPCONFIGFILE` file is defined in the surefire plugin system property
     */
    @Autowired
    OAuthApplicationsConfig oAuthApplicationsConfig;

    @Test
    public void loadingYamlsFromMultipleLocations_should_mergeIntoOneConfigMap() {
        assertThat(oAuthApplicationsConfig.getApplications().size()).isEqualTo(6);
        assertThat(oAuthApplicationsConfig.getApplications())
                .containsKeys("auth0", "auth0-v2", "self-signed");
    }

    @Test
    public void givenDifferentAppName_shouldReturnCorrespondingAppAuthConfig() {
        assertThat(oAuthApplicationsConfig.getApplications().size()).isEqualTo(6);

        OAuthConfig authConfig = oAuthApplicationsConfig.getAppAuthConfig();

        assertThat(authConfig)
                .extracting("domain", "clientId")
                .contains("https://devopsnextgenx.io/", "AJn1FDdQtsuolZeU4MIK2O7NbXvjccKN");
    }


    @Configuration
    public static class TestConfig {

        @Bean
        public RestTemplate restTemplate() {
            return new RestTemplate();
        }
    }

}
