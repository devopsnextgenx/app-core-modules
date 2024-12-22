package io.devopsnextgenx.microservices.modules.models;

import jakarta.persistence.Column;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * BaseModel:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/4/2019
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BaseModel extends BaseModelAudit {
    @Column(name = "owner")
    private String owner;
    @Column(name = "ownerOrganization")
    private String ownerOrganization;
}
