package io.devopsnextgenx.microservices.modules.models;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Role:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/4/2019
 */
@Data
@Builder
@Entity(name="ROLE")
@AllArgsConstructor
@NoArgsConstructor
public class Role extends BaseModelAudit {
    private String name;
}
