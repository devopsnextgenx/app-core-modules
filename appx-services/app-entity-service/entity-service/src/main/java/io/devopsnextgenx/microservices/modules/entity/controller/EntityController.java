package io.devopsnextgenx.microservices.modules.entity.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.devopsnextgenx.microservices.modules.api.EntityServiceApi;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "JWT")
public class EntityController implements EntityServiceApi {

}
