package io.devopsnextgenx.microservices.modules.org.importxml;

import io.devopsnextgenx.microservices.modules.loader.services.AbstractPostImportXmlServiceProcessor;
import io.devopsnextgenx.microservices.modules.loader.services.ImportXmlService;
import io.devopsnextgenx.microservices.modules.security.models.Organization;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;

@Slf4j
@Builder
public class OrgPostXmlImportServiceProcessor extends AbstractPostImportXmlServiceProcessor {
    @Override
    public void postProcessImportXmlService(ImportXmlService importXmlService) {
        ApplicationContext applicationContext = importXmlService.getApplicationContext(importXmlService.getDataLoadFileName());
        applicationContext.getBeansOfType(Organization.class).values().stream().forEach(organization -> {
            try {
                if (organization != null
                        && StringUtils.isNotEmpty(organization.getAdminId())
                        && StringUtils.isNotEmpty(importXmlService.getInternalIdByExternalId(organization.getAdminId()))) {
                    organization.setAdminId(importXmlService.getInternalIdByExternalId(organization.getAdminId()));
                    importXmlService.getCreatorMap().get(Organization.class).createOrUpdate(organization);
                }
            } catch (Exception ex) {
                log.warn("Failed to get InternalId for {}", organization.getAdminId());
            }
        });
    }
}
