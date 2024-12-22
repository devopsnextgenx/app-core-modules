package io.devopsnextgenx.microservices.modules.models;

import lombok.Data;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;

import java.util.Collections;
import java.util.Set;

import org.hibernate.annotations.UuidGenerator;

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
    @Column(unique = true, nullable = false)
    @UuidGenerator
    public String id;

    @Column(name = "isDeleted")
    private boolean deleted;

    @Transient
    private String externalId;

    @Transient
    public Set<Object> getDependents() {
        return Collections.emptySet();
    }
}
