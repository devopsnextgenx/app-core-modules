package io.devopsnextgenx.microservices.modules.user.service;

import io.devopsnextgenx.microservices.modules.models.User;
import io.devopsnextgenx.microservices.modules.models.providers.UserCloner;
import io.devopsnextgenx.microservices.modules.repository.UserRepository;
import io.devopsnextgenx.microservices.modules.userauth.user.dto.UserDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.micrometer.core.annotation.Timed;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

/**
 * UserService:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 2/1/20
 */
public class UserService {
    private UserRepository userRepository;
    private UserCloner userCloner;

    public UserService(UserRepository userRepository, UserCloner userCloner) {
        this.userRepository = userRepository;
        this.userCloner = userCloner;
    }

    // @Autowired
    // private CircuitBreakerFactory<?, ?> circuitBreakerFactory;

    @Timed
    // @HystrixCommand(fallbackMethod = "fallbackMethod")
    @CircuitBreaker(name = "UserService.listAll", fallbackMethod = "fallbackMethod")
    public List<UserDto> listAll() {
        return userRepository.findAll()
        .stream()
        .map(item -> userCloner.cloneToDto(item))
        .collect(Collectors.toList());
    }
    
    // public List<UserDto> listAll() {
    //     CircuitBreaker circuitBreaker = circuitBreakerFactory.create("UserSrvice.listAll");
    //     return circuitBreaker.run(() -> userRepository.findAll()
    //     .stream()
    //     .map(item -> userCloner.cloneToDto(item))
    //     .collect(Collectors.toList()),
    //         throwable -> fallbackMethod(throwable));
    // }

    @Timed
    public UserDto getEntityById(String id) {
        return userCloner.cloneToDto(userRepository.getReferenceById(id));
    }

    @Timed
    public UserDto postEntity(User user) {
        return userCloner.cloneToDto(userRepository.save(user));
    }

    @Timed
    public UserDto putEntity(String id, User user) {
        return userCloner.cloneToDto(userRepository.save(user));
    }

    @Timed
    public UserDto patchEntity(String id, User user) {
        return userCloner.cloneToDto(userRepository.save(user));
    }

    public  List<UserDto> fallbackMethod(Throwable hystrixCommand) {
        return List.of();
    }
}
