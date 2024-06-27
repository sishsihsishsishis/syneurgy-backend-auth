package com.bezkoder.spring.security.postgresql.controllers;


import com.bezkoder.spring.security.postgresql.models.NBehavior;
import com.bezkoder.spring.security.postgresql.repository.NBehaviorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/nbehaviors")
public class NBehaviorController {

    @Autowired
    private NBehaviorRepository nBehaviorRepository;

    @GetMapping
    public List<NBehavior> getAllNBehaviors() {
        return nBehaviorRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<NBehavior> getNBehaviorById(@PathVariable Long id) {
        Optional<NBehavior> nBehavior = nBehaviorRepository.findById(id);
        return nBehavior.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public NBehavior createNBehavior(@RequestBody NBehavior nBehavior) {
        return nBehaviorRepository.save(nBehavior);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NBehavior> updateNBehavior(@PathVariable Long id, @RequestBody NBehavior nBehaviorDetails) {
        Optional<NBehavior> optionalNBehavior = nBehaviorRepository.findById(id);

        if (optionalNBehavior.isPresent()) {
            NBehavior nBehavior = optionalNBehavior.get();
            nBehavior.setDescription(nBehaviorDetails.getDescription());
            // Update other fields as necessary
            NBehavior updatedNBehavior = nBehaviorRepository.save(nBehavior);
            return ResponseEntity.ok(updatedNBehavior);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNBehavior(@PathVariable Long id) {
        Optional<NBehavior> optionalNBehavior = nBehaviorRepository.findById(id);

        if (optionalNBehavior.isPresent()) {
            nBehaviorRepository.delete(optionalNBehavior.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
