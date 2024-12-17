package io.devopsnextgenx.microservices.modules.user.controller;

import io.devopsnextgenx.microservices.modules.user.service.UserService;
import io.devopsnextgenx.microservices.modules.userauth.user.api.UsersApi;
import io.devopsnextgenx.microservices.modules.userauth.user.model.User;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * k8clusters:
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

    @Override
    public ResponseEntity<List<User>> listUsers() {
        return ResponseEntity.ok(userService.listAll());
    }

    @Override
    public ResponseEntity<User> getUserById(String id) {
        return ResponseEntity.ok(userService.getEntityById(id));
    }

    @Override
    public ResponseEntity<User> postUser(User user) {
        return ResponseEntity.ok(userService.postEntity(user));
    }

    @Override
    public ResponseEntity<User> putUserById(String id, @Valid User user) {
        return ResponseEntity.ok(userService.putEntity(id, user));
    }

    @Override
    public ResponseEntity<User> patchUserById(String id, @Valid User user) {
        return ResponseEntity.ok(userService.putEntity(id, user));
    }
}
