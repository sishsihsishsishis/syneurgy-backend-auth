package com.bezkoder.spring.security.postgresql.controllers;



import com.bezkoder.spring.security.postgresql.models.Component;
import com.bezkoder.spring.security.postgresql.repository.ComponentRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/components1")
@Api(tags = "Component")
public class ComponentController {
    @Autowired
    ComponentRepository componentRepository;


    @GetMapping
    @ApiOperation("Get All Components")
    public ResponseEntity<?> getAllComponents(@RequestParam(required = false) String search) {
        List<Component> components = new ArrayList<>();
        componentRepository.findAllByOrderByIdAsc().forEach(components::add);
        if (search != null && !search.isEmpty()) {
            components = components.stream()
                    .filter(component -> component.getName().toLowerCase().contains(search.toLowerCase()))
                    .toList(); // Requires Java 16 or later; use toList() from Collectors in earlier versions
        }
        if (components.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(components, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ApiOperation("Get component by id.")
    public ResponseEntity<?> getComponentById(@PathVariable Long id) {

        Optional<Component> component = componentRepository.findById(id);
        return component.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ApiOperation("Create Component")
    public ResponseEntity<?> createComponent(@RequestBody Component component) {
        return ResponseEntity.ok(componentRepository.save(component));
    }

    @PutMapping("/{id}")
    @ApiOperation("Update component by id")
    public ResponseEntity<?> updateComponent(@PathVariable Long id, @RequestBody Component updatedComponent) {

        Optional<Component> existingComponent = componentRepository.findById(id);
        if (existingComponent.isPresent()) {
            updatedComponent.setId(id);
            return ResponseEntity.ok(componentRepository.save(updatedComponent));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Delete Component By id")
    public ResponseEntity<?> deleteComponent(@PathVariable Long id) {

        Optional<Component> component = componentRepository.findById(id);
        if (component.isPresent()) {
            componentRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
