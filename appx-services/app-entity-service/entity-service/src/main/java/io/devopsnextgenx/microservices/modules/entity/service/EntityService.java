package io.devopsnextgenx.microservices.modules.entity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EntityService {

    @Autowired
    private RestTemplate restTemplate;

}
