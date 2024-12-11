package io.devopsnextgenx.microservices.modules.security.jwt;

import com.auth0.jwt.impl.PublicClaims;
import io.devopsnextgenx.microservices.modules.security.jwt.helpers.OAuthLoginHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@TestPropertySource(properties = { "spring.config.location = classpath:auth-config-test.yaml" })
@ContextConfiguration(classes = { SelfSignedTokenValidatorConfig.class, JwtSecurityConfiguration.class,
        SelfSignedJwtFilterTest.TestConfig.class })
public class SelfSignedJwtFilterTest {

    @Autowired
    TokenValidator tokenValidator;

    @Autowired
    JwtTokenAuthenticationFilter jwtFilter;

    @Mock
    HttpServletRequest request;

    @Autowired
    OAuthLoginHelper loginHelper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void verifySelfSignedTokenValidator_isInjectedToContext() {
        assertThat(tokenValidator).isExactlyInstanceOf(SelfSignedTokenValidator.class);
    }

    @Test
    public void authenticateSelfSigned() throws ServletException, IOException {

        String testSubjectName = "unknown-user@some-domain.com";
        String token = loginHelper.login(testSubjectName);
        log.info("SelfSigned Token: {}", token);
        when(request.getHeader(eq(AuthHeaders.AUTHORIZATION_HEADER))).thenReturn(token);

        jwtFilter.doFilterInternal(request, null, new EmptyChain());

        AccessDataAuthenticationToken authentication = (AccessDataAuthenticationToken) SecurityContextHolder
                .getContext().getAuthentication();
        assertThat(authentication).isNotNull();
        assertThat(authentication.getPrincipal()).isEqualTo(testSubjectName);
        assertThat(authentication.isFederated()).isFalse(); // no user array claim at all.
    }

    @Test
    public void givenFederatedUser_whenTokenHasFederatedClaim_accessDataShouldHaveFlagFederatedTrue()
            throws ServletException, IOException {

        String testSubjectName = "unknown-user@some-domain.com";
        @SuppressWarnings("unchecked")
        String token = loginHelper.login(testSubjectName, Pair.of(JwtTokenAuthenticationFilter.CLAIM_USER_ARRAY,
                Collections.singletonMap(JwtTokenAuthenticationFilter.CLAIM_FEDERATED, true)));
        when(request.getHeader(eq(AuthHeaders.AUTHORIZATION_HEADER))).thenReturn(token);

        jwtFilter.doFilterInternal(request, null, new EmptyChain());

        AccessDataAuthenticationToken authentication = (AccessDataAuthenticationToken) SecurityContextHolder
                .getContext().getAuthentication();
        assertThat(authentication.isFederated()).isTrue();
    }

    @Test
    public void givenRegularUserWithUserArrayClaim_whenNoFederatedClaimExists_accessDataShouldHaveFlagFederatedFalse()
            throws ServletException, IOException {

        String testSubjectName = "unknown-user@some-domain.com";
        @SuppressWarnings("unchecked")
        String token = loginHelper.login(testSubjectName, Pair.of(JwtTokenAuthenticationFilter.CLAIM_USER_ARRAY,
                Collections.singletonMap("SomethingElse", true)));
        when(request.getHeader(eq(AuthHeaders.AUTHORIZATION_HEADER))).thenReturn(token);

        jwtFilter.doFilterInternal(request, null, new EmptyChain());

        AccessDataAuthenticationToken authentication = (AccessDataAuthenticationToken) SecurityContextHolder
                .getContext().getAuthentication();
        assertThat(authentication.isFederated()).isFalse();
    }

    @Test
    public void when_validTokenHeader__controllersShouldReturnResponse() throws Exception {

        String testSubjectName = "test@some-domain.com";
        String token = loginHelper.login(testSubjectName, (Pair<String, Object>[]) null);
        when(request.getHeader(eq(AuthHeaders.AUTHORIZATION_HEADER))).thenReturn(token);

        this.mockMvc.perform(get("/api/test").header(HttpHeaders.AUTHORIZATION, token)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(TestController.TEST_REPLY_FROM_CONTROLLER)));

    }

    @Test
    public void when_invalidTokenHeader__controllersShouldFailUnAuthorized() throws Exception {

        this.mockMvc.perform(get("/api/test").header(HttpHeaders.AUTHORIZATION, "some.invalid.token")).andDo(print())
                .andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void when_tokenExpired__controllersShouldFailTokenExpiredException() throws Exception {

        @SuppressWarnings("unchecked")
        String token = loginHelper.login("unknown-user@some-domain.com",
                Pair.of(PublicClaims.EXPIRES_AT, new Date(10000)));
        System.out.println(token);
        when(request.getHeader(eq(AuthHeaders.AUTHORIZATION_HEADER))).thenReturn(token);

        this.mockMvc.perform(get("/api/test").header(HttpHeaders.AUTHORIZATION, token)).andDo(print())
                .andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
    }

    class EmptyChain implements FilterChain {

        @Override
        public void doFilter(ServletRequest request, ServletResponse response) {
        }
    }

    @Configuration
    public static class TestConfig {
        @Bean
        public RestTemplate restTemplate() {
            return new RestTemplate();
        }

        @Bean
        public TestController testController() {
            return new TestController();
        }
    }
}
