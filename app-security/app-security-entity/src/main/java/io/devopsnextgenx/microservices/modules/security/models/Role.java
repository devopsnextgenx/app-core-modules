package io.devopsnextgenx.microservices.modules.security.models;

import org.springframework.security.core.GrantedAuthority;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import io.devopsnextgenx.microservices.modules.access.model.ROLE;
import io.devopsnextgenx.microservices.modules.models.BaseModelAudit;

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
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="ROLE")
public class Role extends BaseModelAudit implements GrantedAuthority {
    private ROLE name;
    
    @Override
    public String getAuthority() {
        return this.name.toString();
    }
}
