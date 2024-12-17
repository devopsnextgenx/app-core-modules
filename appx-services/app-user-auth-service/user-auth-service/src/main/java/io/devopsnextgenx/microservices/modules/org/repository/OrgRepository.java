package io.devopsnextgenx.microservices.modules.org.repository;

import io.devopsnextgenx.microservices.modules.org.models.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrgRepository extends JpaRepository<Organization, String> {
}
