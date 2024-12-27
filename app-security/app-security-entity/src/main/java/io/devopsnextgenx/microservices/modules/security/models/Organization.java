package io.devopsnextgenx.microservices.modules.security.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Entity;

import io.devopsnextgenx.microservices.modules.models.BaseModelAudit;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="ORGANIZATION")
public class Organization extends BaseModelAudit {
    private String orgName;
    private boolean active;
    private String adminId;
}
