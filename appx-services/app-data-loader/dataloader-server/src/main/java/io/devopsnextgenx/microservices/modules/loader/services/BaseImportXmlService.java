package io.devopsnextgenx.microservices.modules.loader.services;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.task.DelegatingSecurityContextTaskExecutor;

import io.devopsnextgenx.microservices.modules.access.model.IAuthenticationFacade;
import io.devopsnextgenx.microservices.modules.exception.AppException;
import io.devopsnextgenx.microservices.modules.model.Entity;
import io.devopsnextgenx.microservices.modules.model.EntityList;
import io.devopsnextgenx.microservices.modules.model.XmlImporter;
import io.devopsnextgenx.microservices.modules.models.BaseEntity;
import io.devopsnextgenx.microservices.modules.models.IdMapper;
import io.devopsnextgenx.microservices.modules.repository.IdMapperRepository;
import io.devopsnextgenx.microservices.modules.utils.creators.Creator;

import java.util.*;

/**
 * BaseImportXmlService:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/4/2019
 */
@Data
@Slf4j
public abstract class BaseImportXmlService {
    public Map<String, Creator> creatorMap;
    public IAuthenticationFacade authenticationFacade;
    private List<AbstractPostImportXmlServiceProcessor> postImportXmlServiceProcessorList;
    private ApplicationContext beans;

    public abstract IdMapperRepository getIdMapperRepository();

    private int getNumberOfAllElements(Map<Integer, List<BaseEntity>> m) {
        int rtn = 0;
        for (List<BaseEntity> l: m.values()) {
            rtn += l.size();
        }
        return rtn;
    }
    private List<Class> excluded = Arrays.asList();

    /**
     * return map of all types in the ApplicationContext where the key is
     * the number of the depened objects for the class
     */
    private Map<Integer, List<BaseEntity>> getTypeOrderedByNumOfDependents(Map<String, BaseEntity> allTypes) {
        Map<Integer, List<BaseEntity>> types = new HashMap<>();
        allTypes.forEach((beanId, be) -> {
            List<BaseEntity> t;
            if(!excluded.contains(be.getClass())){
                t = types.computeIfAbsent(be.getDependents()!=null ?be.getDependents().size(): 0, k -> new ArrayList<>());
                t.add(be);
            } else{
            }
        });
        return types;
    }

    /**
     * XML files contain ExternalId and are not duplicated,
     * if there is an error the test will not continue the Recreate/Update
     *
     */
    private void checkExternalIdAppearanceAndDuplications(Map<Integer, List<BaseEntity>> mappedTypes) throws AppException {
        Map<String, BaseEntity> mapAll = new HashMap<>();
        for(List<BaseEntity> list:mappedTypes.values()) {
            for (BaseEntity be : list) {
                Creator c = getCreator(be);
                if (c != null) {
                    List<String> externalIds = c.getExternalIds(be);
                    for (String externalId : externalIds) {
                        if (externalId == null) {
                            throw new AppException("The element %s or sub elements is missing External ID, please add External ID attribute",
                                    be.getExternalId() != null ? be.getExternalId() : be.toString());
                        } else {
                            if (mapAll.get(externalId) != null) {
                                throw new AppException("The ExternalId %s or sub elements is Duplicated, please change External ID attribute", be.getExternalId());
                            } else {
                                mapAll.put(externalId, be);
                            }
                        }
                    }
                }
            }
        }
    }

    private Creator getCreator(BaseEntity be) {
        return creatorMap.get(be.getClass().getSimpleName());
    }

    public XmlImporter recreateDBWithMicroService(String mode, ApplicationContext beans) {
        this.reportLog("RecreateDB Initiated by : %s", getAuthenticationFacade().getUserName());

        List<String> errorsList = new ArrayList<>();
        List<Entity> entityList = new ArrayList<>();
        XmlImporter xmlImporter = new XmlImporter();
        xmlImporter.setNotCreated(new EntityList());
        int elementsToCreate = 0;
        int createdElements = 0;
        int updatedElements = 0;
        int lastCreated = -1;
        try {
            Map<Integer, List<BaseEntity>> mappedTypes = getTypeOrderedByNumOfDependents(beans.getBeansOfType(BaseEntity.class));

            elementsToCreate = getNumberOfAllElements(mappedTypes);
            checkExternalIdAppearanceAndDuplications(mappedTypes);


            ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
            taskExecutor.setCorePoolSize(4);
            taskExecutor.setMaxPoolSize(5);
            taskExecutor.setQueueCapacity(50);
            taskExecutor.initialize();
            DelegatingSecurityContextTaskExecutor delegatingSecurityContextTaskExecutor =
                    new DelegatingSecurityContextTaskExecutor(taskExecutor, SecurityContextHolder.getContext());

            mappedTypes.values().stream().forEach(entitys -> {
                entitys.forEach(entity -> {
                    delegatingSecurityContextTaskExecutor.execute(getParallelTask(entity));
                });
            });

            while(createdElements < elementsToCreate && lastCreated != createdElements){ // if lastCreated == created then nothing was created in the iteration
                this.reportLog("******************** Start create/update iteration ****************************", errorsList.toString());
                lastCreated = createdElements;
                for(List<BaseEntity> list: mappedTypes.values()){
                    Iterator<BaseEntity> iterator = list.iterator();
                    while (iterator.hasNext()) {
                        BaseEntity be = iterator.next();
                        if(be.getExternalId() != null){
                            Creator creator = getCreator(be);
                            if(creator != null) {
                                try {
                                    IdMapper idMapper = creator.getIdMapperByExternalId(be.getExternalId());
                                    boolean update = false;
                                    if (idMapper != null) {
                                        be.setId(idMapper.getExternalId());
                                        update = true;
                                    }
                                    if (creator.prepared(be)) {
                                        BaseEntity baseEntity = null;
                                        if (idMapper == null || isOverride(mode)) {
                                            baseEntity = creator.createOrUpdate(be);
                                        } else {
                                            be.setId(idMapper.getId());
                                            baseEntity = creator.loadFromDb(be);
                                        }
                                        String id = getIdFromBaseEntityOrCarrier(baseEntity);
                                        if (id != null) {
                                            baseEntity.setExternalId(be.getExternalId());
                                            if (!update || isOverride(mode)) {
                                                creator.saveIdsToMapper(baseEntity);
                                                if (update) {
                                                    updatedElements++;
                                                } else {
                                                    createdElements++;
                                                }
                                            }
                                            iterator.remove();
                                        }
                                    }
                                } catch (Exception e){
                                    this.reportLog("Error creating the entity of type %s with externalId: %s", be.getClass().getSimpleName(), be.getExternalId(), e);
                                    errorsList.add(be.getExternalId());
                                    Entity entity = new Entity();
                                    entity.setExternalId(be.getExternalId());
                                    String message = "%s";
                                    entity.setMessage(String.format(message, getCauseMessage(e)));
                                    entityList.add(entity);
                                    iterator.remove();
                                }
                            }else{
                                this.reportLog("Creator not found for %s with External Id: %s", be.getClass().getSimpleName(), be.getExternalId());
                                iterator.remove();
                            }
                        }else{
                            this.reportLog("Missing external ID for %s", be.getClass().getSimpleName());
                        }
                    }
                }
            }

            this.reportLog("#############################################################################################");
            this.reportLog("## There were %s entities to createOrUpdate, created:%s, created:%s, Not created:%s, ERRORS:%s  ##",
                    elementsToCreate, createdElements, updatedElements, elementsToCreate - createdElements - updatedElements, errorsList.size());
            this.reportLog("#############################################################################################");

        } catch (Exception ex) {
            this.reportLog("Failed processing RecreateDB", errorsList.toString());
            this.reportLog("ex = %s" , ex.getMessage());
            xmlImporter.setError(ex.getMessage());
        } finally {
            this.reportLog("Errors: %s", errorsList.toString());
            xmlImporter.setToCreate(elementsToCreate);
            xmlImporter.setCreated(createdElements);
            xmlImporter.setUpdated(updatedElements);
            xmlImporter.getNotCreated().setCount(elementsToCreate - createdElements);
            xmlImporter.getNotCreated().setEntities(entityList);
        }
        return xmlImporter;
    }

    private boolean isOverride(String mode) {
        return mode.replaceAll("\"", "").equals("OVERRIDE");
    }

    private String getCauseMessage(Exception e) {
        StringBuilder msg = new StringBuilder(e.getMessage());
        msg.insert(0, e.getCause() != null ? e.getCause().getMessage() + " -> " : "");
        return msg.insert(0, e.getCause().getCause() !=null ? e.getCause().getCause().getMessage()+ " -> "  : "").toString() ;
    }

    protected void reportLog(String msg, Object... args){
        if (msg == null) {
            msg = "";
        }
        String message = String.format(msg, args);
        log.info(message);
    }

    private String getIdFromBaseEntityOrCarrier(Object be)  {
        if((be instanceof BaseEntity))
            return ((BaseEntity) be).getId();
        return null;
    }

    public void processEntitySave(List<BaseEntity> entityMap) {
        entityMap.stream().forEach(baseEntity -> {
            creatorMap.get(baseEntity.getClass().getSimpleName()).createOrUpdate(baseEntity);
        });
    }

    private Runnable getParallelTask(BaseEntity entity) {
        return () -> {
            log.info("[{}] Started task {}. Thread: {}",
                    SecurityContextHolder.getContext().getAuthentication().getName(), entity.getClass().getSimpleName(),
                    Thread.currentThread().getName());
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("[{}] Ended task {}. Thread: {}",
                    SecurityContextHolder.getContext().getAuthentication().getName(), entity.getClass().getSimpleName(),
                    Thread.currentThread().getName());
        };
    }

//    public void checkXmlRoleAccess(){
//        AccessData accessData = ((AccessDataAuthenticationToken) getAuthenticationFacade().getAuthentication()).getAccessData();
//        if(accessData == null){
//            throw new K8RepoException(K8RepoException.ERROR_CODE.ACCESS_DENIED, "Access Data is missing for creating users from XML.");
//        }
//        if(!accessData.getUserRoles().contains(Role.SYSTEM_ADMINISTRATOR)){
//            throw new K8RepoException(K8RepoException.ERROR_CODE.ACCESS_DENIED, "User %s can't access this API", accessData.getUserId());
//        }
//    }


    public String getInternalIdByExternalId(String externalId){
        IdMapper idMapper = getIdMapperRepository().findByExternalId(externalId);
        if(idMapper == null){
            throw new AppException("No Entity with external id %s was found", externalId);
        }
        return idMapper.getId();
    }

    public ApplicationContext getApplicationContext(String xmlFilePath) {
        if (beans == null ){
            beans = new ClassPathXmlApplicationContext(xmlFilePath);
        }
        return beans;
    }

    public abstract String getDataLoadFileName();
}
