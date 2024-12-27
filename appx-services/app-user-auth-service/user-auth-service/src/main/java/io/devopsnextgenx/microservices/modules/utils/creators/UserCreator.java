package io.devopsnextgenx.microservices.modules.utils.creators;

import io.devopsnextgenx.microservices.modules.models.BaseEntity;
import io.devopsnextgenx.microservices.modules.security.models.User;
import io.devopsnextgenx.microservices.modules.security.repositories.AppxUserRepository;
import io.devopsnextgenx.microservices.modules.repository.IdMapperRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    private AppxUserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    public UserCreator(IdMapperRepository idMapperRepository, AppxUserRepository userRepository, PasswordEncoder passwordEncoder) {
        setIdMapperRepository(idMapperRepository);
        setUserRepository(userRepository);
        setPasswordEncoder(passwordEncoder);
    }

    @Override
    public BaseEntity createOrUpdate(BaseEntity be) {
        BaseEntity rtn = null;
        User user = (User) be;
        if(isNotExisting(be)) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
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
