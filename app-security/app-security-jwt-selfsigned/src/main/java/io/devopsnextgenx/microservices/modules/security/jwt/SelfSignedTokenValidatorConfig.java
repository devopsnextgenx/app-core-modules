package io.devopsnextgenx.microservices.modules.security.jwt;

import io.devopsnextgenx.microservices.modules.security.jwt.cache.JWTVerifierCache;
import io.devopsnextgenx.microservices.modules.security.jwt.config.OAuthApplicationsConfig;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
@AutoConfigureBefore(JwtSecurityConfiguration.class)
@ConditionalOnExpression(value = "#{${appx.modules.security.jwt.enabled:false} and ${appx.modules.security.jwt.selfSigned.enabled:false}}")
public class SelfSignedTokenValidatorConfig {

    @Bean
    @Primary
    @ConditionalOnProperty(prefix = "appx.modules.security.oauth", name = "defaultAuthType", havingValue = "SELFSIGNED")
    TokenValidator selfSignedTokenValidator(OAuthApplicationsConfig oAuthApplicationsConfig,
            JWTVerifierCache jwtVerifierCache) {
        return new SelfSignedTokenValidator(oAuthApplicationsConfig, jwtVerifierCache);
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
        .build();
    }

    @Bean
    public JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider();
    }
}
