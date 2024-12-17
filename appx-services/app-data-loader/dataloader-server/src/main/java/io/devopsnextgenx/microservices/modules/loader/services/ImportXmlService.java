package io.devopsnextgenx.microservices.modules.loader.services;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

import io.devopsnextgenx.microservices.modules.access.model.IAuthenticationFacade;
import io.devopsnextgenx.microservices.modules.dto.EntityDto;
import io.devopsnextgenx.microservices.modules.dto.EntityListDto;
import io.devopsnextgenx.microservices.modules.dto.XmlImporterDto;
import io.devopsnextgenx.microservices.modules.loader.configuration.DataLoaderProperties;
import io.devopsnextgenx.microservices.modules.repository.IdMapperRepository;
import io.devopsnextgenx.microservices.modules.utils.creators.Creator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ImportXmlService:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/4/2019
 */
@Data
@Slf4j
@EqualsAndHashCode(callSuper=false)
public class ImportXmlService extends BaseImportXmlService {
    private DataLoaderProperties dataLoaderProperties;
    private IdMapperRepository idMapperRepository;


    public ImportXmlService(Map<String, Creator> creatorMap, DataLoaderProperties dataLoaderProperties,
                            IdMapperRepository idMapperRepository, IAuthenticationFacade authenticationFacade,
                            List<AbstractPostImportXmlServiceProcessor> postImportXmlServiceProcessorList) {
        setCreatorMap(creatorMap);
        setDataLoaderProperties(dataLoaderProperties);
        setIdMapperRepository(idMapperRepository);
        setAuthenticationFacade(authenticationFacade);
        setPostImportXmlServiceProcessorList(postImportXmlServiceProcessorList);
    }

    @Override
    public IdMapperRepository getIdMapperRepository() {
        return idMapperRepository;
    }

    public XmlImporterDto xmlImport(String mode){
        XmlImporterDto xmlImporter = new XmlImporterDto();
        List<String> errorsList = new ArrayList<>();
        List<EntityDto> entityList = new ArrayList<>();
        xmlImporter.setNotCreated(new EntityListDto());
        int elementsToCreate = 0;
        int createdElements = 0;
        int updatedElements = 0;
        try {
            ApplicationContext beans = getApplicationContext(getDataLoadFileName());
            xmlImporter = recreateDBWithMicroService(mode, beans);
            postProcessImportXmlService();
        } catch (Exception ex) {
            this.reportLog("Failed processing RecreateDB", errorsList.toString());
            this.reportLog("ex = %s" , ex.getMessage());
            xmlImporter.setError(ex.getMessage());
            xmlImporter.setToCreate(elementsToCreate);
            xmlImporter.setCreated(createdElements);
            xmlImporter.setUpdated(updatedElements);
            xmlImporter.getNotCreated().setCount(elementsToCreate - createdElements);
            xmlImporter.getNotCreated().setEntities(entityList);
        }
        return xmlImporter;
    }

    private void postProcessImportXmlService() {
        try {
            getPostImportXmlServiceProcessorList().stream().forEach(postProcessor-> {
                postProcessor.postProcessImportXmlService(this);
            });
        } catch (Exception ex) {
            log.error("Post processing failed for the ImportXmlService", ex);
        }
    }

    @Override
    public String getDataLoadFileName() {
        return dataLoaderProperties.getDataLoadFile();
    }

}
