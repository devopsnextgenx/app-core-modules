package io.devopsnextgenx.microservices.modules.user.creators;

import io.devopsnextgenx.microservices.modules.utils.creators.Creator;
import io.devopsnextgenx.microservices.modules.models.BaseEntity;
import io.devopsnextgenx.microservices.modules.repository.IdMapperRepository;
import io.devopsnextgenx.microservices.modules.user.models.User;
import io.devopsnextgenx.microservices.modules.user.repository.UserRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * UserCreator:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/4/2019
 */
@Data
@Slf4j
public class UserCreator extends Creator {

    private UserRepository userRepository;
    public UserCreator(IdMapperRepository idMapperRepository, UserRepository userRepository) {
        setIdMapperRepository(idMapperRepository);
        setUserRepository(userRepository);
    }

    @Override
    public BaseEntity createOrUpdate(BaseEntity be) {
        BaseEntity rtn = null;
        User user = (User) be;
        if(isNotExisting(be)) {
            rtn = userRepository.save(user);
            be.setId(rtn.getId());
        } else {
            rtn = userRepository.findById(be.getId()).get();
            be.setId(rtn.getId());
            rtn = userRepository.save(user);
        }
        User userRtn = (User) rtn;
        if (userRtn.getOrganization()!=null
                && StringUtils.isNotEmpty(userRtn.getOrganization().getAdminId())
                && userRtn.getOrganization().getAdminId().equalsIgnoreCase(be.getExternalId())) {
            userRtn.getOrganization().setAdminId(userRtn.getId());
            userRepository.save(userRtn);
        }
        return rtn;
    }

    @Override
    public BaseEntity loadFromDb(BaseEntity be) {
        return userRepository.getReferenceById(be.getId());
    }

    @Override
    public boolean prepared(BaseEntity be){
        boolean prepared = false;
        if (be != null) {
            User user = (User) be;
            prepared = !user.getUserRoles().stream().anyMatch(role ->
                getIdMapperByExternalId(role.getExternalId())==null
            );
            if (user.getOrganization()!=null) {
                prepared = prepared ? getIdMapperByExternalId(user.getOrganization().getExternalId()) != null : !prepared;
            }
        }
        return prepared;
    }
}
