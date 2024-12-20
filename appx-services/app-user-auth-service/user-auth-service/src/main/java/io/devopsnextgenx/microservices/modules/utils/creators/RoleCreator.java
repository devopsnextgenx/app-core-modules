package io.devopsnextgenx.microservices.modules.utils.creators;

import io.devopsnextgenx.microservices.modules.models.BaseEntity;
import io.devopsnextgenx.microservices.modules.models.Role;
import io.devopsnextgenx.microservices.modules.repositories.RoleRepository;
import io.devopsnextgenx.microservices.modules.repository.IdMapperRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * UserRoleCreator:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/4/2019
 */
@Data
@Slf4j
public class RoleCreator extends Creator {

    private RoleRepository roleRepository;
    public RoleCreator(IdMapperRepository idMapperRepository, RoleRepository roleRepository) {
        setIdMapperRepository(idMapperRepository);
        setRoleRepository(roleRepository);
    }

    @Override
    public BaseEntity createOrUpdate(BaseEntity be) {
        BaseEntity rtn = null;
        if(isNotExisting(be)) {
            rtn = roleRepository.save((Role) be);
            be.setId(rtn.getId());
        } else {
            rtn = roleRepository.findById(be.getId()).get();
            be.setId(rtn.getId());
            rtn = roleRepository.save((Role) be);
        }
        return rtn;
    }

    @Override
    public BaseEntity loadFromDb(BaseEntity be) {
        return roleRepository.getReferenceById(be.getId());
    }
}
