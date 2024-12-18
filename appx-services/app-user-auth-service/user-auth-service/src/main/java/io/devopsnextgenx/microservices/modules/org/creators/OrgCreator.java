package io.devopsnextgenx.microservices.modules.org.creators;

import io.devopsnextgenx.microservices.modules.utils.creators.Creator;
import io.devopsnextgenx.microservices.modules.models.BaseEntity;
import io.devopsnextgenx.microservices.modules.org.models.Organization;
import io.devopsnextgenx.microservices.modules.org.repository.OrgRepository;
import io.devopsnextgenx.microservices.modules.repository.IdMapperRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * OrgCreator:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/4/2019
 */
@Data
@Slf4j
public class OrgCreator extends Creator {

    private OrgRepository orgRepository;
    public OrgCreator(IdMapperRepository idMapperRepository, OrgRepository orgRepository) {
        setIdMapperRepository(idMapperRepository);
        setOrgRepository(orgRepository);
    }

    @Override
    public BaseEntity createOrUpdate(BaseEntity be) {
        BaseEntity rtn = null;
        if(isNotExisting(be)) {
            rtn = orgRepository.save((Organization) be);
            be.setId(rtn.getId());
        } else {
            rtn = orgRepository.findById(be.getId()).get();
            be.setId(rtn.getId());
            rtn = orgRepository.save((Organization) be);
        }
        return rtn;
    }

    @Override
    public BaseEntity loadFromDb(BaseEntity be) {
        return orgRepository.getReferenceById(be.getId());
    }

    @Override
    public boolean prepared(BaseEntity be){
        return true;
    }
}
