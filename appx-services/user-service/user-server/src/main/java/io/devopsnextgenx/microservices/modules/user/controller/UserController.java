package io.devopsnextgenx.microservices.modules.user.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.devopsnextgenx.microservices.modules.api.UserApi;
import io.devopsnextgenx.microservices.modules.dto.UserDto;
import io.devopsnextgenx.microservices.modules.models.User;
import io.devopsnextgenx.microservices.modules.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api")
public class UserController implements UserApi {
    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/echo", method = {RequestMethod.GET}, produces = {MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<String> echo() {
        log.info("echo endpoint invoked");
        return new ResponseEntity<>("{'message': 'User'}", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<UserDto> getUserById(UUID id) {
        User user = userRepository.getReferenceById(id.toString());
        return ResponseEntity.ok(new UserDto()
        .email(user.getEmail())
        .firstName(user.getFirstName())
        .lastName(user.getLastName())
        .id(UUID.fromString(user.getId())));
    }

    @Override
    public ResponseEntity<Void> deleteUserById(UUID id) {
        return ResponseEntity.status(201).body(null);
    }

    
}
