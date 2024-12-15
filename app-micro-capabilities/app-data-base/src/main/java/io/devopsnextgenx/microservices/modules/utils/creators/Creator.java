package io.devopsnextgenx.microservices.modules.utils.creators;

import lombok.Data;
import org.springframework.context.ApplicationContext;

import io.devopsnextgenx.microservices.modules.models.BaseEntity;
import io.devopsnextgenx.microservices.modules.models.IdMapper;
import io.devopsnextgenx.microservices.modules.repository.IdMapperRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Creator:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/4/2019
 */
@Data
public abstract class Creator {
    protected static ApplicationContext beans;
    private IdMapperRepository idMapperRepository;

    /**
     * create or update entity
     * if the given entity contain id not null, it will update else it will create
     *
     * @param be
     * @return
     */
    public abstract BaseEntity createOrUpdate(BaseEntity be);


    public boolean isNotExisting(BaseEntity be) {
        boolean notExists = true;
        IdMapper idMapperByExternalId = getIdMapperByExternalId(be.getExternalId());
        if (idMapperByExternalId!=null){
            be.setId(idMapperByExternalId.getId());
            notExists = false;
        }
        return notExists;
    }

    /**
     * return true if the given entity is ready to create, it will fill the
     * ref entities related to the relevant entity
     *
     * @param be
     * @return
     */
    public boolean prepared(BaseEntity be){
        return be != null;
    }

    /**
     * return the list of all ExternalId's related to the given element
     *
     * @param be
     * @return list of all related ExternalId's
     */
    public List<String> getExternalIds(BaseEntity be){
        return  new ArrayList(Arrays.asList(be.getExternalId()));
    }

    /**
     * fetch data from database according to the unique properties
     *
     * @param be
     * @return
     */
    public abstract BaseEntity loadFromDb(BaseEntity be);

    /**
     * save all idMappers that created in the element to idMapper
     *
     */
    public void saveIdsToMapper(BaseEntity be){
        saveIdToMapper(be.getId(), be.getExternalId());
    }

    public void saveIdToMapper(String id, String externalId) {
        saveIdToMapper(new IdMapper(externalId, id));
    }

    public void saveIdToMapper(IdMapper im) {
        if (getIdMapperByExternalId(im.getExternalId())==null) {
            idMapperRepository.save(im);
        }
    }

    public IdMapper getIdMapperByExternalId(String externalId) {
        Optional<IdMapper> byId = idMapperRepository.findById(externalId);
        return idMapperRepository.findByExternalId(externalId);
    }

    public void setUserBeans(ApplicationContext userBeans) {
        if (this.beans == null) {
            this.beans = userBeans;
        }
    }

    protected ApplicationContext getBeans() {
        return this.beans;
    }

}
