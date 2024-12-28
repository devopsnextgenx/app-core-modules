package io.devopsnextgenx.microservices.modules.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

import java.sql.Timestamp;

/**
 * BaseModel:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/4/2019
 */
@Data
@MappedSuperclass
@EqualsAndHashCode(callSuper = true)
public class BaseModelAudit extends BaseEntity {
    @Column(name = "creationDate")
    private Timestamp creationDate;
    @Column(name = "modifiedDate")
    private Timestamp modifiedDate;

    private String createdBy;
    private String updatedBy;
}
