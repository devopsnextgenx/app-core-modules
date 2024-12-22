package io.devopsnextgenx.microservices.modules.user.service;

import io.devopsnextgenx.microservices.modules.models.User;
import io.devopsnextgenx.microservices.modules.models.providers.UserCloner;
import io.devopsnextgenx.microservices.modules.repositories.AppxUserRepository;
import io.devopsnextgenx.microservices.modules.userauth.user.dto.UserDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

/**
 * UserService:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 2/1/20
 */
@Slf4j
public class UserService {
    private AppxUserRepository userRepository;
    private UserCloner userCloner;

    public UserService(AppxUserRepository userRepository, UserCloner userCloner) {
        this.userRepository = userRepository;
        this.userCloner = userCloner;
    }

    @Timed
    @CircuitBreaker(name = "UserService.listAllResilient", fallbackMethod = "fallbackMethod")
    public List<UserDto> listAllResilient() throws TimeoutException {
        double test = Math.random();
        if (test < 0.4) {
            log.info("Throwing error: 000000000000000000000000000000000000000");
            throw new TimeoutException();
        } else if(test < 0.55) {
            try {
                log.info("Sleeping: xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        List<UserDto> userList = userRepository.findAll()
        .stream()
        .map(item -> userCloner.cloneToDto(item))
        .collect(Collectors.toList());

        return userList;
    }
    
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

    public void deleteUserById(String id) {
        userRepository.deleteById(id);
    }

    public  List<UserDto> fallbackMethod(Throwable hystrixCommand) {
        return List.of();
    }
}
