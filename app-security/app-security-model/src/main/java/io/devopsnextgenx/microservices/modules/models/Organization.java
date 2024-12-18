package io.devopsnextgenx.microservices.modules.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


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
