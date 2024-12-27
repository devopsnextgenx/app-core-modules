package io.devopsnextgenx.microservices.modules.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import io.devopsnextgenx.microservices.modules.security.models.User;
import io.devopsnextgenx.microservices.modules.principal.AppxUserPrincipal;
import io.devopsnextgenx.microservices.modules.security.repositories.AppxUserRepositoryImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class AppxUserDetailsService implements UserDetailsService {
    private AppxUserRepositoryImpl userRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        log.info("UserDetailsService: {}", user.getEmail());
        return new AppxUserPrincipal(user);
    }

    public boolean existUser(String userName) {
        return userRepository.existUser(userName);
    }
    
    public void createUser(User user) {
        userRepository.save(user);
    }
}