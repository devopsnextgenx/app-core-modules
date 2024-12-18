package io.devopsnextgenx.microservices.modules.user.service;

import io.devopsnextgenx.microservices.modules.user.models.User;
import io.devopsnextgenx.microservices.modules.user.models.UserCloner;
import io.devopsnextgenx.microservices.modules.user.repository.UserRepository;
import io.devopsnextgenx.microservices.modules.userauth.user.dto.UserDto;
import io.micrometer.core.annotation.Timed;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * UserService:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 2/1/20
 */
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;
    private UserCloner userCloner;

    @Timed
    public List<UserDto> listAll() {
        return userRepository.findAll()
                .stream()
                .map(item -> userCloner.cloneToDto(item))
                .collect(Collectors.toList());
    }

    @Timed
    public UserDto getEntityById(String id) {
        return userCloner.cloneToDto(userRepository.getReferenceById(id));
    }

    public UserDto postEntity(User user) {
        return userCloner.cloneToDto(userRepository.save(user));
    }

    public UserDto putEntity(String id, User user) {
        return userCloner.cloneToDto(userRepository.save(user));
    }

    public UserDto patchEntity(String id, User user) {
        return userCloner.cloneToDto(userRepository.save(user));
    }
}
