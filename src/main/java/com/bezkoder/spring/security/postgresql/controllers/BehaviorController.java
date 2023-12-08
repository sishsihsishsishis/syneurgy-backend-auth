package com.bezkoder.spring.security.postgresql.controllers;

import com.bezkoder.spring.security.postgresql.models.Behavior;
import com.bezkoder.spring.security.postgresql.models.Superpower;
import com.bezkoder.spring.security.postgresql.repository.BehaviorRepository;
import com.bezkoder.spring.security.postgresql.service.BehaviorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/behaviors")
@Api(tags = "Behavior")
public class BehaviorController {
    @Autowired
    BehaviorRepository behaviorRepository;

    @Autowired
    BehaviorService behaviorService;

    @GetMapping("/byComponent/{componentId}")
    public ResponseEntity<?> getBehaviorsByComponentId(@PathVariable Long componentId) {
        List<Behavior> behaviors = behaviorService.getBehaviorsByComponentId(componentId);
        if (behaviors.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(behaviors, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ApiOperation("Get behavior by id")
    public ResponseEntity<?> getBehaviorById(@PathVariable Long id) {
        Optional<Behavior> behavior = behaviorRepository.findById(id);
        return behavior.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
