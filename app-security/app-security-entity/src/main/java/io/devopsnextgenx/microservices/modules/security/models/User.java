package io.devopsnextgenx.microservices.modules.security.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import io.devopsnextgenx.microservices.modules.utils.converters.PasswordEncryptor;
import io.devopsnextgenx.microservices.modules.access.model.ROLE;
import io.devopsnextgenx.microservices.modules.models.BaseModelAudit;

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
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="USER")
public class User extends BaseModelAudit {
    @Column(unique = true, name = "userName")
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private boolean active;

    @Convert(converter = PasswordEncryptor.class)
    private String password;

    @ManyToOne(fetch=FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name="organizationId", nullable=false)
    private Organization organization;

    @ManyToMany(
        cascade = {
            CascadeType.PERSIST, 
            CascadeType.MERGE
        },
        fetch = FetchType.EAGER
    )
    @JoinTable(
            name = "USERS_ROLES",
            joinColumns = @JoinColumn(
                    name = "userId", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "roleId", referencedColumnName = "id"))
    private List<Role> userRoles;

    @Transient
    private List<ROLE> roles;
}
