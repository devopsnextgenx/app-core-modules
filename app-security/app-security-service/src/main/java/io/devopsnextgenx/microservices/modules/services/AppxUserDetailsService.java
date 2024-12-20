package io.devopsnextgenx.microservices.modules.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import io.devopsnextgenx.microservices.modules.models.User;
import io.devopsnextgenx.microservices.modules.principal.AppxUserPrincipal;
import io.devopsnextgenx.microservices.modules.repositories.AppxUserRepositoryImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class AppxUserDetailsService implements UserDetailsService {
    private AppxUserRepositoryImpl userRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username) {
        log.info("UserDetailsService: " + username);
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new AppxUserPrincipal(user);
    }

    public boolean existUser(String userName) {
        return userRepository.existUser(userName);
    }
    
    public void createUser(User user) {
        userRepository.save(user);
    }
}