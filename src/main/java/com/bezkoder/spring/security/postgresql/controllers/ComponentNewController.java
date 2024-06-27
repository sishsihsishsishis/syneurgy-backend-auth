package com.bezkoder.spring.security.postgresql.controllers;




import com.bezkoder.spring.security.postgresql.models.ComponentNew;
import com.bezkoder.spring.security.postgresql.repository.ComponentNewRepository;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/components-new")
public class ComponentNewController {

    @Autowired
    private ComponentNewRepository componentNewRepository;

    @GetMapping
    @ApiOperation("Get All Components")
    public ResponseEntity<?> getAllComponents(@RequestParam(required = false) String search) {
        List<ComponentNew> componentNews = new ArrayList<>();
        componentNewRepository.findAllByOrderByIdAsc().forEach(componentNews::add);
        if (search != null && !search.isEmpty()) {
            componentNews = componentNews.stream()
                    .filter(componentNew -> componentNew.getName().toLowerCase().contains(search.toLowerCase()))
                    .toList();
        }
        if (componentNews.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(componentNews, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComponentNew> getComponentById(@PathVariable Long id) {
        Optional<ComponentNew> component = componentNewRepository.findById(id);
        return component.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ComponentNew createComponent(@RequestBody ComponentNew componentNew) {
        return componentNewRepository.save(componentNew);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ComponentNew> updateComponent(@PathVariable Long id, @RequestBody ComponentNew componentNewDetails) {
        Optional<ComponentNew> optionalComponent = componentNewRepository.findById(id);

        if (optionalComponent.isPresent()) {
            ComponentNew componentNew = optionalComponent.get();
            componentNew.setName(componentNewDetails.getName());
            componentNew.setDescription(componentNewDetails.getDescription());
            // Update other fields as necessary
            ComponentNew updatedComponent = componentNewRepository.save(componentNew);
            return ResponseEntity.ok(updatedComponent);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComponent(@PathVariable Long id) {
        Optional<ComponentNew> optionalComponent = componentNewRepository.findById(id);

        if (optionalComponent.isPresent()) {
            componentNewRepository.delete(optionalComponent.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
