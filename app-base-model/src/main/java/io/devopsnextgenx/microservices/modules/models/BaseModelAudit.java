package io.devopsnextgenx.microservices.modules.models;

import lombok.Data;

import jakarta.persistence.Column;

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
public class BaseModelAudit extends BaseEntity {
    @Column(name = "creationDate")
    private Timestamp creationDate;
    @Column(name = "lastModifiedDate")
    private Timestamp lastModifiedDate;

}
