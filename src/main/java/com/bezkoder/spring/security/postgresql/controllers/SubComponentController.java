package com.bezkoder.spring.security.postgresql.controllers;



import com.bezkoder.spring.security.postgresql.models.SubComponent;
import com.bezkoder.spring.security.postgresql.repository.SubComponentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/subcomponents")
public class SubComponentController {

    @Autowired
    private SubComponentRepository subComponentRepository;

    @GetMapping
    public List<SubComponent> getAllSubComponents() {
        return subComponentRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubComponent> getSubComponentById(@PathVariable Long id) {
        Optional<SubComponent> subComponent = subComponentRepository.findById(id);
        return subComponent.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public SubComponent createSubComponent(@RequestBody SubComponent subComponent) {
        return subComponentRepository.save(subComponent);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubComponent> updateSubComponent(@PathVariable Long id, @RequestBody SubComponent subComponentDetails) {
        Optional<SubComponent> optionalSubComponent = subComponentRepository.findById(id);

        if (optionalSubComponent.isPresent()) {
            SubComponent subComponent = optionalSubComponent.get();
            subComponent.setName(subComponentDetails.getName());
            subComponent.setCv(subComponentDetails.getCv());
            subComponent.setNlp(subComponentDetails.getNlp());
            // Update other fields as necessary
            SubComponent updatedSubComponent = subComponentRepository.save(subComponent);
            return ResponseEntity.ok(updatedSubComponent);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubComponent(@PathVariable Long id) {
        Optional<SubComponent> optionalSubComponent = subComponentRepository.findById(id);

        if (optionalSubComponent.isPresent()) {
            subComponentRepository.delete(optionalSubComponent.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
