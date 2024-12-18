package io.devopsnextgenx.microservices.modules.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.devopsnextgenx.microservices.modules.user.models.User;
import jakarta.transaction.Transactional;

/**
 * io.k8clusters.auth.repo.repository.UserRepository:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/4/2019
 */
@Transactional
@Repository
public interface UserRepository extends JpaRepository<User, String> {

}
