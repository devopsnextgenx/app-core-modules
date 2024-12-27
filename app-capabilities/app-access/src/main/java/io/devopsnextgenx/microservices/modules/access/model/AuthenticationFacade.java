package io.devopsnextgenx.microservices.modules.access.model;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import jakarta.annotation.PostConstruct;
import lombok.Data;

/**
 * appx:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 2/1/20
 */
@Data
public class AuthenticationFacade implements IAuthenticationFacade {

    private SecurityContext securityContext;

    @PostConstruct
    public void populateSecurityContext() {
        this.securityContext = SecurityContextHolder.getContext();
    }

    @Override
    public String getUserName() {
        return securityContext.getAuthentication().getName();
    }
}
