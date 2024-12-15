package io.k8s.framework.controller;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.k8s.framework.api.UserApi;
import io.k8s.framework.model.User;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api")
public class UserController implements UserApi {

    @RequestMapping(value = "/echo", method = {RequestMethod.GET}, produces = {MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<String> echo() {
        log.info("echo endpoint invoked");
        return new ResponseEntity<>("{'message': 'User'}", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<User> getUserById(UUID id) {
        User user = new User().email("test").firstName("amit").lastName("kshirsagar").id(id);
        return ResponseEntity.ok(user);
    }

    @Override
    public ResponseEntity<Void> deleteUserById(UUID id) {
        return ResponseEntity.status(201).body(null);
    }

    
}
