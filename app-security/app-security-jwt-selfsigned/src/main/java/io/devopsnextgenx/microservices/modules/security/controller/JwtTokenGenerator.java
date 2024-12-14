package io.devopsnextgenx.microservices.modules.security.controller;

import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import io.devopsnextgenx.microservices.modules.security.jwt.JwtTokenProvider;
import io.devopsnextgenx.microservices.modules.security.models.AuthRequest;
import io.devopsnextgenx.microservices.modules.security.models.JwtResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/basic/api")
@SecurityRequirement(name = "basicAuth")
@ConditionalOnProperty(value = "app.modules.security.jwt.selfSigned.enableController", havingValue = "true", matchIfMissing = false)
public class JwtTokenGenerator {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private HttpServletRequest request;

    @PostMapping("/generate-token")
    @Operation(summary = "Generate JWT Token", description = "Generate JWT Token")
    public ResponseEntity<JwtResponse> generateTokenPost(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

        String token = jwtTokenProvider.createToken(authentication);
        return ResponseEntity.ok(JwtResponse.builder().accessToken(token).refreshToken(token).build());
    }

    @GetMapping("/generate-token")
    @Operation(summary = "Generate JWT Token", description = "Generate JWT Token")
    public ResponseEntity<JwtResponse> generateTokenGet() {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Basic ")) {
            String base64Credentials = authHeader.substring("Basic ".length()).trim();
            byte[] decodedBytes = Base64.getDecoder().decode(base64Credentials);
            String credentials = new String(decodedBytes);

            // Split the credentials into username and password
            String[] parts = credentials.split(":");
            if (parts.length == 2) {
                String username = parts[0];
                String password = parts[1];

                Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));
                String token = jwtTokenProvider.createToken(authentication);
                return ResponseEntity.ok(JwtResponse.builder().accessToken(token).refreshToken(token).build());
            }
        }
        return ResponseEntity.badRequest().build();
    }
}

