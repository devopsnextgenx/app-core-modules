package io.devopsnextgenx.microservices.modules.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import io.devopsnextgenx.microservices.modules.models.Organization;

public interface OrgRepository extends JpaRepository<Organization, String> {
}
