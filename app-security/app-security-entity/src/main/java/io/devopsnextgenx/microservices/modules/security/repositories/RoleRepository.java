package io.devopsnextgenx.microservices.modules.security.repositories;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.devopsnextgenx.microservices.modules.security.models.Role;

/**
 * RoleRepository:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/4/2019
 */
@Repository
@Transactional
public interface RoleRepository extends JpaRepository<Role, String> {

}
