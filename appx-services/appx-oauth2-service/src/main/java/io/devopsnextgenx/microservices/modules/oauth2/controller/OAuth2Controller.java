package io.devopsnextgenx.microservices.modules.oauth2.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.SessionAttributes;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
@Controller
@RequestMapping("/oauth2")
@SessionAttributes("user")
public class OAuth2Controller {

    @GetMapping("/success")
    public String handleGitHubCallback(@AuthenticationPrincipal OAuth2User principal) {
        // Handle the callback and extract the access token
        // String accessToken = principal.getAttribute("access_token");
        OAuth2AccessToken accessToken = principal.getAuthorities().stream()
        .filter(grantedAuthority -> grantedAuthority instanceof OAuth2AccessToken)
        .map(grantedAuthority -> (OAuth2AccessToken) grantedAuthority)
        .findFirst()
        .orElseThrow(() -> new RuntimeException("Access token not found"));
        // Use the access token for GitHub API requests or store it as needed
        // Redirect or display user information as required
        extractJwtToken(accessToken);
        return "redirect:/oauth2/success"; // Redirect to a profile page, for example
    }

    // Helper method to extract JWT from the access token
    private String extractJwtToken(OAuth2AccessToken accessToken) {
        // Implement logic to extract JWT from the access token
        // This will depend on the structure of the access token provided by GitHub
        System.out.println("=====================================================================================================");
        System.out.println(accessToken.getTokenValue());
        System.out.println("=====================================================================================================");
        return null;
    }
}
