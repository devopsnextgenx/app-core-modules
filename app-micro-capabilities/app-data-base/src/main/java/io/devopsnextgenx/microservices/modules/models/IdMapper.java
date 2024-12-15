package io.devopsnextgenx.microservices.modules.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * IdMapper:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/4/2019
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "K8CLUSTER_ID_MAPPER")
public class IdMapper {
    @Id
    @Column(name = "EXTERNAL_ID")
    public String externalId;

    @Column(name = "ENTITY_ID")
    public String id;
}
