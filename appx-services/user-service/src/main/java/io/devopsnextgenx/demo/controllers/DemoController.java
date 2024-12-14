package io.devopsnextgenx.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.devopsnextgenx.demo.models.Demo;
import io.devopsnextgenx.demo.services.DemoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.List;

/**
 * The DemoController class is a Spring MVC controller that handles HTTP
 * requests
 * for the demo application. It provides endpoints for various functionality
 * related to the demo application.
 */
@RestController
@RequestMapping("/jwt/api")
@SecurityRequirement(name = "JWT")
public class DemoController {

    @Autowired
    private DemoService demoService;

    @PostMapping("/demos")
    @Operation(summary = "Add Demo Entity", description = "Returns a new demo entity created with posted body")
    public ResponseEntity<Demo> createDemo(@RequestBody Demo demo) {
        Demo savedDemo = demoService.createDemo(demo);
        return new ResponseEntity<>(savedDemo, HttpStatus.CREATED);
    }

    @GetMapping("/demos")
    @Operation(summary = "List Demo Entities", description = "Returns a List of all Demo Entities")
    public ResponseEntity<List<Demo>> getAllDemos() {
        List<Demo> demos = demoService.getAllDemos();
        return new ResponseEntity<>(demos, HttpStatus.OK);
    }

    @GetMapping("/demos/{id}")
    @Operation(summary = "Get Demo Entity", description = "Returns the Demo Entity with Id")
    public ResponseEntity<Demo> getDemoById(@PathVariable Long id) {
        return new ResponseEntity<>(demoService.getDemoById(id), HttpStatus.OK);
    }

    @PutMapping("/demos/{id}")
    @Operation(summary = "Put Demo Entity", description = "Update the Demo Entity with Id")
    public ResponseEntity<Demo> updateDemo(@PathVariable Long id, @RequestBody Demo demo) {
        return new ResponseEntity<>(demoService.updateDemo(id, demo), HttpStatus.OK);
    }

    @DeleteMapping("/demos/{id}")
    @Operation(summary = "Delete Demo Entity", description = "Delete the Demo Entity with Id")
    public ResponseEntity<Void> deleteDemo(@PathVariable Long id) {
        demoService.deleteDemo(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
