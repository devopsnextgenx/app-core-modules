package io.devopsnextgenx.microservices.modules.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import io.devopsnextgenx.microservices.modules.models.User;
import jakarta.transaction.Transactional;

/**
 * UserRepository:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/4/2019
 */
@Transactional
public interface AppxUserRepository extends JpaRepository<User, String> {
}
