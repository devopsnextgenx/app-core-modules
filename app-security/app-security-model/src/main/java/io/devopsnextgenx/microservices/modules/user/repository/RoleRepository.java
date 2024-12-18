package io.devopsnextgenx.microservices.modules.user.repository;

import io.devopsnextgenx.microservices.modules.user.models.Role;
import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * RoleRepository:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/4/2019
 */
@Transactional
@Repository
public interface RoleRepository extends JpaRepository<Role, String> {

}
