package io.devopsnextgenx.microservices.modules.principal;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import io.devopsnextgenx.microservices.modules.models.User;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AppxUserPrincipal implements UserDetails {
    private User user;
    public AppxUserPrincipal(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getUserRoles();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserName();
    }

    @Override
    public boolean isEnabled() {
        return user.isActive();
    }
}