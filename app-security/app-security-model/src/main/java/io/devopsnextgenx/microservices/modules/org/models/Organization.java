package io.devopsnextgenx.microservices.modules.org.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import io.devopsnextgenx.microservices.modules.models.BaseModelAudit;
import jakarta.persistence.Entity;

@Data
@Builder
@Entity(name="ORGANIZATION")
@NoArgsConstructor
@AllArgsConstructor
public class Organization extends BaseModelAudit {
    private String orgName;
    private boolean active;
    private String adminId;
}
