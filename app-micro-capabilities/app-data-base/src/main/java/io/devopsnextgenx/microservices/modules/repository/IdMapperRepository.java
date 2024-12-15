package io.devopsnextgenx.microservices.modules.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import io.devopsnextgenx.microservices.modules.models.IdMapper;

/**
 * IdMapperRepository:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/4/2019
 */
@Repository
public interface IdMapperRepository extends JpaRepository<IdMapper, String> {
    @Query
    IdMapper findByExternalId(String externalId);
}
