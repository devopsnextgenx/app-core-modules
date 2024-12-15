package io.devopsnextgenx.microservices.modules.models;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.UUIDHexGenerator;

import java.io.Serializable;

/**
 * K8UUIDGenerator:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/4/2019
 */
public class AppUIDGenerator extends UUIDHexGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object obj) {
        Serializable id = (Serializable) session.getEntityPersister(null, obj)
                .getClassMetadata().getIdentifier(obj, session);
        return id != null ? id : (Serializable) super.generate(session, obj);
    }
}