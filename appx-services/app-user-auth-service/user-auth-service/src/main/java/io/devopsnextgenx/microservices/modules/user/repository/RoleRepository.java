package io.devopsnextgenx.microservices.modules.user.repository;

import io.devopsnextgenx.microservices.modules.user.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * io.k8clusters.auth.repo.repository.UserRepository:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/4/2019
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, String> {

}
