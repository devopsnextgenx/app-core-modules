package io.devopsnextgenx.microservices.modules.user.service;

import io.devopsnextgenx.microservices.modules.user.repository.UserRepository;
import io.micrometer.core.annotation.Timed;
import lombok.AllArgsConstructor;
import io.devopsnextgenx.microservices.modules.userauth.user.model.User;

import java.util.List;

/**
 * k8clusters:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 2/1/20
 */
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;

    @Timed
    public List<User> listAll() {
        return userRepository.findAll();
    }

    @Timed
    public User getEntityById(String id) {
        return userRepository.getReferenceById(id);
    }

    public User postEntity(User user) {
        return userRepository.save(user);
    }

    public User putEntity(String id, User user) {
        return userRepository.save(user);
    }

    public User patchEntity(String id, User user) {
        return userRepository.save(user);
    }
}
