package io.devopsnextgenx.microservices.modules.auth.controller;

import io.devopsnextgenx.microservices.modules.auth.service.AuthService;
import io.devopsnextgenx.microservices.modules.userauth.auth.api.AuthServiceApi;
import io.devopsnextgenx.microservices.modules.userauth.auth.model.AuthToken;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * AuthController:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/6/2019
 */
@RestController
@RequestMapping("/")
public class AuthController implements AuthServiceApi {

    @Autowired
    private AuthService authService;

    @Override
    public ResponseEntity<AuthToken> authToken(@NotNull @Valid String code) {
        return ResponseEntity.ok(authService.getAccessTokenFromGrantCode(code));
    }
}
