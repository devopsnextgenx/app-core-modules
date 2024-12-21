package io.devopsnextgenx.microservices.modules.user.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.json.mgmt.Role;

import io.devopsnextgenx.microservices.modules.api.UserApi;
import io.devopsnextgenx.microservices.modules.dto.RoleDto;
import io.devopsnextgenx.microservices.modules.dto.UserDto;
import io.devopsnextgenx.microservices.modules.models.User;
import io.devopsnextgenx.microservices.modules.repositories.AppxUserRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "JWT")
public class UserController implements UserApi {
    @Autowired
    private AppxUserRepository userRepository;

    @RequestMapping(value = "/echo", method = {RequestMethod.GET}, produces = {MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<String> echo() {
        log.info("echo endpoint invoked");
        return new ResponseEntity<>("{'message': 'User'}", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<UserDto> getUserById(UUID id) {
        User user = userRepository.getReferenceById(id.toString());
        List<RoleDto> roles = user.getUserRoles().stream().map(role -> RoleDto.fromValue(role.getName().name())).toList();
        return ResponseEntity.ok(new UserDto()
        .email(user.getEmail())
        .firstName(user.getFirstName())
        .lastName(user.getLastName())
        .userName(user.getUserName())
        .roles(roles)
        .id(user.getId()));
    }

    @Override
    public ResponseEntity<Void> deleteUserById(UUID id) {
        return ResponseEntity.status(201).body(null);
    }

    
}
