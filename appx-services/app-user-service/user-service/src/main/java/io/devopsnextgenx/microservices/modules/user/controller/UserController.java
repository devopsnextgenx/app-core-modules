package io.devopsnextgenx.microservices.modules.user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.devopsnextgenx.microservices.modules.api.UserServiceApi;
import io.devopsnextgenx.microservices.modules.dto.RoleDto;
import io.devopsnextgenx.microservices.modules.dto.UserDto;
import io.devopsnextgenx.microservices.modules.models.User;
import io.devopsnextgenx.microservices.modules.repositories.AppxUserRepository;
import io.devopsnextgenx.microservices.modules.user.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "JWT")
public class UserController implements UserServiceApi {
    @Autowired
    private AppxUserRepository userRepository;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/echo", method = {RequestMethod.GET}, produces = {MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<String> echo() {
        log.info("echo endpoint invoked");
        return new ResponseEntity<>("{'message': 'User'}", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<UserDto> getUserById(String id) {
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
    public ResponseEntity<List<UserDto>> listUsers() {
        return ResponseEntity.ok(userService.listUsersLoadBalancer());
    }

    @Override
    public ResponseEntity<UserDto> postUser(UserDto userDto) {
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setUserName(userDto.getUserName());
        // Assuming roles are set elsewhere or need to be converted from RoleDto
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
    }

    @Override
    public ResponseEntity<UserDto> putUserById(String id, UserDto userDto) {
        User user = userRepository.getReferenceById(id);
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setUserName(userDto.getUserName());
        // Assuming roles are set elsewhere or need to be converted from RoleDto
        userRepository.save(user);
        return ResponseEntity.ok(userDto);
    }

    @Override
    public ResponseEntity<UserDto> patchUserById(String id, UserDto userDto) {
        User user = userRepository.getReferenceById(id);
        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }
        if (userDto.getFirstName() != null) {
            user.setFirstName(userDto.getFirstName());
        }
        if (userDto.getLastName() != null) {
            user.setLastName(userDto.getLastName());
        }
        if (userDto.getUserName() != null) {
            user.setUserName(userDto.getUserName());
        }
        // Assuming roles are set elsewhere or need to be converted from RoleDto
        userRepository.save(user);
        return ResponseEntity.ok(userDto);
    }

    @Override
    public ResponseEntity<Void> deleteUserById(String id) {
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
