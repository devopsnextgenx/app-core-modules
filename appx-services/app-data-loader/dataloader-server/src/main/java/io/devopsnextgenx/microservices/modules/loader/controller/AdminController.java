package io.devopsnextgenx.microservices.modules.loader.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.devopsnextgenx.microservices.modules.api.ImportXmlApi;
import io.devopsnextgenx.microservices.modules.model.XmlImporter;
import io.devopsnextgenx.microservices.modules.loader.services.ImportXmlService;

/**
 * AdminController:
 *
 * @author Amit Kshirsagar
 * @version 1.0
 * @Modifications Added initial revision of the application
 * @since 12/4/2019
 */

@RestController
@RequestMapping("/api")
public class AdminController implements ImportXmlApi {
    @Autowired
    private ImportXmlService importXmlService;

    @Override
    public ResponseEntity<XmlImporter> importXml(String mode) {
        XmlImporter xmlImporter = importXmlService.xmlImport(mode);
        return ResponseEntity.ok(xmlImporter);
    }
}
