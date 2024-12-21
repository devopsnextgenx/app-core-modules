package io.devopsnextgenx.microservices.modules.user.controller;

import io.devopsnextgenx.microservices.modules.models.providers.UserCloner;
import io.devopsnextgenx.microservices.modules.user.service.UserService;
import io.devopsnextgenx.microservices.modules.userauth.user.api.UserServiceApi;
import io.devopsnextgenx.microservices.modules.userauth.user.dto.UserDto;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * UserController:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 2/1/20
 */
@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "JWT")
public class UserController implements UserServiceApi {

    @Autowired
    private UserService userService;

    @Autowired
    private UserCloner userCloner;

    @Override
    public ResponseEntity<List<UserDto>> listUsers() {
        try {
            return ResponseEntity.ok(userService.listAll());
        } catch (TimeoutException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return (ResponseEntity<List<UserDto>>) ResponseEntity.internalServerError();
    }

    @Override
    public ResponseEntity<UserDto> getUserById(String id) {
        return ResponseEntity.ok(userService.getEntityById(id));
    }

    @Override
    public ResponseEntity<UserDto> postUser(UserDto user) {
        return ResponseEntity.ok(userService.postEntity(userCloner.cloneToModel(user)));
    }

    @Override
    public ResponseEntity<UserDto> putUserById(String id, @Valid UserDto user) {
        return ResponseEntity.ok(userService.putEntity(id,userCloner.cloneToModel(user)));
    }

    @Override
    public ResponseEntity<UserDto> patchUserById(String id, @Valid UserDto user) {
        return ResponseEntity.ok(userService.putEntity(id,userCloner.cloneToModel(user)));
    }
}
