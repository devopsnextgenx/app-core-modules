package io.devopsnextgenx.microservices.modules.user.models;

import io.devopsnextgenx.microservices.modules.models.BaseModelAudit;
import io.devopsnextgenx.microservices.modules.org.models.Organization;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * User:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/4/2019
 */
@Data
@Builder
@Entity(name="USER")
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseModelAudit {
    @Column(unique = true, name = "userName")
    private String userName;
    private String email;
    private boolean active;

    @ManyToOne(fetch=FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name="organizationId", nullable=false)
    private Organization organization;

    @ManyToMany
    @JoinTable(
            name = "USERS_ROLES",
            joinColumns = @JoinColumn(
                    name = "userId", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "roleId", referencedColumnName = "id"))
    private List<Role> userRoles;

}
