package io.devopsnextgenx.microservices.modules.models;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;

import java.util.Set;

/**
 * BaseEntity:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/4/2019
 */
@Data
@MappedSuperclass
public class BaseEntity {
    @Id
    @GenericGenerator(name = "AppUIDGenerator", strategy = "io.devopsnextgenx.microservices.modules.models.AppUIDGenerator")
    @GeneratedValue(generator = "AppUIDGenerator", strategy = GenerationType.IDENTITY)
    @Column(unique=true, nullable=false)
    public String id;

    @Column(name = "isDeleted")
    private boolean deleted;

    @Transient
    private String externalId;

    @Transient
    public Set getDependents() {
        return null;
    }
}
