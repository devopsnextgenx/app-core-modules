package io.devopsnextgenx.microservices.modules.user.controller;

import io.devopsnextgenx.microservices.modules.user.models.UserCloner;
import io.devopsnextgenx.microservices.modules.user.service.UserService;
import io.devopsnextgenx.microservices.modules.userauth.user.api.UsersApi;
import io.devopsnextgenx.microservices.modules.userauth.user.dto.UserDto;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
public class UserController implements UsersApi {

    @Autowired
    private UserService userService;

    @Autowired
    private UserCloner userCloner;

    @Override
    public ResponseEntity<List<UserDto>> listUsers() {
        return ResponseEntity.ok(userService.listAll());
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
