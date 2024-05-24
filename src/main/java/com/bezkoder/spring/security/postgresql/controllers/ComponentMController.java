package com.bezkoder.spring.security.postgresql.controllers;

import com.bezkoder.spring.security.postgresql.models.ComponentM;
import com.bezkoder.spring.security.postgresql.repository.ComponentMRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/m/components")
public class ComponentMController {

    @Autowired
    private ComponentMRepository componentMRepository;

    @GetMapping
    public List<ComponentM> getAllComponents() {
        return componentMRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComponentM> getComponentById(@PathVariable Long id) {
        Optional<ComponentM> componentM = componentMRepository.findById(id);
        if (componentM.isPresent()) {
            return ResponseEntity.ok(componentM.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ComponentM createComponent(@RequestBody ComponentM componentM) {
        return componentMRepository.save(componentM);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ComponentM> updateComponent(@PathVariable Long id, @RequestBody ComponentM componentMDetails) {
        Optional<ComponentM> optionalComponentM = componentMRepository.findById(id);
        if (optionalComponentM.isPresent()) {
            ComponentM componentM = optionalComponentM.get();
            componentM.setComponent(componentMDetails.getComponent());
            componentM.setSubcomponent(componentMDetails.getSubcomponent());
            componentM.setCv(componentMDetails.getCv());
            componentM.setNlp(componentMDetails.getNlp());
            componentM.setBehaviors(componentMDetails.getBehaviors());
            ComponentM updatedComponent = componentMRepository.save(componentM);
            return ResponseEntity.ok(updatedComponent);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComponent(@PathVariable Long id) {
        if (componentMRepository.existsById(id)) {
            componentMRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
