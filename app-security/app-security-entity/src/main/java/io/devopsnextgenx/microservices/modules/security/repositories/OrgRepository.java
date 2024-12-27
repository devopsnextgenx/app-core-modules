package io.devopsnextgenx.microservices.modules.security.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import io.devopsnextgenx.microservices.modules.security.models.Organization;

public interface OrgRepository extends JpaRepository<Organization, String> {
}
