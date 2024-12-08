package io.devopsnextgenx.microservices.modules.app.services.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/v1")
public class SampleController {
    @GetMapping(value = "helloWorld", produces = MediaType.TEXT_PLAIN_VALUE)
    public String helloWorld() {
        return "Hello World!!!";
    }
}
