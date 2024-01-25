package com.bezkoder.spring.security.postgresql.controllers;


import com.bezkoder.spring.security.postgresql.exception.UserComponentNotFoundException;
import com.bezkoder.spring.security.postgresql.models.*;
import com.bezkoder.spring.security.postgresql.payload.request.ComponentRequest;
import com.bezkoder.spring.security.postgresql.payload.request.UserComponentRequest;
import com.bezkoder.spring.security.postgresql.payload.response.MessageResponse;
import com.bezkoder.spring.security.postgresql.payload.response.UserComponentResponse;
import com.bezkoder.spring.security.postgresql.repository.ComponentRepository;
import com.bezkoder.spring.security.postgresql.repository.UserComponentRepository;
import com.bezkoder.spring.security.postgresql.repository.UserRepository;
import com.bezkoder.spring.security.postgresql.security.jwt.JwtUtils;
import com.bezkoder.spring.security.postgresql.service.UserComponentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user_component")
@Api(tags = "User Component")
public class UserComponentController {
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    UserRepository userRepository;

    @Autowired
    ComponentRepository componentRepository;

    @Autowired
    UserComponentRepository userComponentRepository;

    @Autowired
    private UserComponentService userComponentService;

    @PostMapping
    @ApiOperation("Create new user component")
    public ResponseEntity<?> createBEWizard(@RequestHeader(name = "Authorization") String token) {
        String username = jwtUtils.getExistingUsername(token);
        Optional<User> existingUser1 = userRepository.findByUsername(username);

        if (existingUser1.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("The current user is unavailable!"));
        }
        User currentUser = existingUser1.get();

        List<Component> componentList = componentRepository.findAllByOrderByIdAsc();

        if(componentList.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("The components are unavailable!"));
        }
        Component firstComponent = componentList.get(0);
        UserComponent userComponent = new UserComponent(currentUser, firstComponent);
        UserComponent newUserComponent = userComponentRepository.save(userComponent);
        return ResponseEntity.ok(new UserComponentResponse(newUserComponent.getId(), newUserComponent.getComponent().getId(), newUserComponent.getUser().getId(), newUserComponent.getCreatedDate().getTime(), newUserComponent.isFinished(), newUserComponent.getStep(), newUserComponent.getAnchorId(), newUserComponent.getBehaviorId(), newUserComponent.getCelebrationId()));
    }

    @PostMapping("/withComponent")
    @ApiOperation("Create new user component with the existing component")
    public ResponseEntity<?> createBEWizardWithComponentId(@RequestHeader(name = "Authorization") String token, @RequestBody UserComponentRequest userComponentRequest) {
        String username = jwtUtils.getExistingUsername(token);
        Optional<User> existingUser1 = userRepository.findByUsername(username);

        if (existingUser1.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("The current user is unavailable!"));
        }
        User currentUser = existingUser1.get();
        Long componentId = userComponentRequest.getComponentId();

        Optional<Component> component = componentRepository.findById(componentId);
        if (component.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("The current component is unavailable!"));
        }
        Component existingComponent = component.get();

        UserComponent userComponent = new UserComponent(currentUser, existingComponent);
        UserComponent newUserComponent = userComponentRepository.save(userComponent);
        return ResponseEntity.ok(new UserComponentResponse(newUserComponent.getId(), newUserComponent.getComponent().getId(), newUserComponent.getUser().getId(), newUserComponent.getCreatedDate().getTime(), newUserComponent.isFinished(), newUserComponent.getStep(), newUserComponent.getAnchorId(), newUserComponent.getBehaviorId(), newUserComponent.getCelebrationId()));
    }

    @PutMapping("/{id}")
    @ApiOperation("Update User Component By id")
    public ResponseEntity<?> updateComponent(@PathVariable Long id, @RequestBody ComponentRequest componentRequest) {

        Optional<UserComponent> userComponent = userComponentRepository.findById(id);
        if (userComponent.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("The current user component is unavailable!"));
        }

        UserComponent uComponent = userComponent.get();
        Long newId = componentRequest.getId();
        Optional<Component> component = componentRepository.findById(newId);
        if(component.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("The current component is unavailable!"));
        }
        Component component1 = component.get();
        uComponent.setComponent(component1);

        UserComponent updatedUserComponent = userComponentRepository.save(uComponent);
        return ResponseEntity.ok(new UserComponentResponse(updatedUserComponent.getId(), updatedUserComponent.getComponent().getId(), updatedUserComponent.getUser().getId(), updatedUserComponent.getCreatedDate().getTime(), updatedUserComponent.isFinished(), updatedUserComponent.getStep(), updatedUserComponent.getAnchorId(), updatedUserComponent.getBehaviorId(), updatedUserComponent.getCelebrationId()));

    }

    @GetMapping
    @ApiOperation("Get User Components of the user")
    public ResponseEntity<?> getUserComponents(@RequestHeader(name = "Authorization") String token) {
        String username = jwtUtils.getExistingUsername(token);
        Optional<User> existingUser = userRepository.findByUsername(username);

        if (existingUser.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("The current user is unavailable!"));
        }

        User currentUser = existingUser.get();
        List<UserComponent> userComponents = userComponentRepository.findByUserId(currentUser.getId());
        int count = userComponents.size();

        return ResponseEntity.ok(count);
    }

    @GetMapping("/{userComponentId}")
    @ApiOperation("Get last User Component of the user")
    public ResponseEntity<?> getLastUserComponent(@PathVariable Long userComponentId) {
        Optional<UserComponent> userComponent = userComponentRepository.findById(userComponentId);
        if (userComponent.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("The current user component is unavailable!"));
        }
        UserComponent uComponent = userComponent.get();
        return ResponseEntity.ok(new UserComponentResponse(uComponent.getId(), uComponent.getComponent().getId(), uComponent.getUser().getId(), uComponent.getCreatedDate().getTime(), uComponent.isFinished(), uComponent.getStep(), uComponent.getAnchorId(), uComponent.getBehaviorId(), uComponent.getCelebrationId()));
    }

    @GetMapping("/last")
    @ApiOperation("Get last User Component of the user")
    public ResponseEntity<?> getLastUserComponent(@RequestHeader(name = "Authorization") String token) {
        String username = jwtUtils.getExistingUsername(token);
        Optional<User> existingUser = userRepository.findByUsername(username);

        if (existingUser.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("The current user is unavailable!"));
        }

        User currentUser = existingUser.get();

        List<UserComponent> components = userComponentRepository.findUnfinishedUserComponentsByUserId(currentUser.getId());

        if (components.isEmpty()) {
            return ResponseEntity.ok("There's nothing");

        }

        UserComponent component = components.get(0);
        return ResponseEntity.ok(new UserComponentResponse(component.getId(), component.getComponent().getId(), component.getUser().getId(), component.getCreatedDate().getTime(), component.isFinished(), component.getStep(), component.getAnchorId(), component.getBehaviorId(), component.getCelebrationId()));
    }

    @PutMapping("/mark-as-finished/{userComponentId}")
    public ResponseEntity<?> markComponentAsFinished(@PathVariable Long userComponentId) {
        try {
            UserComponent updatedUserComponent = userComponentService.markComponentAsFinished(userComponentId);
            return ResponseEntity.ok(new UserComponentResponse(updatedUserComponent.getId(), updatedUserComponent.getComponent().getId(), updatedUserComponent.getUser().getId(), updatedUserComponent.getCreatedDate().getTime(), updatedUserComponent.isFinished(), updatedUserComponent.getStep(), updatedUserComponent.getAnchorId(), updatedUserComponent.getBehaviorId(), updatedUserComponent.getCelebrationId()));
        } catch (UserComponentNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update-step/{userComponentId}")
    public ResponseEntity<?> updateStep(@PathVariable Long userComponentId, @RequestParam int newStep) {
        try {
            UserComponent updatedUserComponent = userComponentService.updateStep(userComponentId, newStep);
            return ResponseEntity.ok(new UserComponentResponse(updatedUserComponent.getId(), updatedUserComponent.getComponent().getId(), updatedUserComponent.getUser().getId(), updatedUserComponent.getCreatedDate().getTime(), updatedUserComponent.isFinished(), updatedUserComponent.getStep(), updatedUserComponent.getAnchorId(), updatedUserComponent.getBehaviorId(), updatedUserComponent.getCelebrationId()));
        } catch (UserComponentNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }



    @PutMapping("/update/{userComponentId}")
    public ResponseEntity<?> updateUserComponent(@PathVariable Long userComponentId, @RequestBody UserComponentRequest userComponentRequest) {
        Optional<UserComponent> userComponent = userComponentRepository.findById(userComponentId);
        if (userComponent.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("The current user component is unavailable!"));
        }

        UserComponent uComponent = userComponent.get();

        Long anchorId = userComponentRequest.getAnchorId();
        if (anchorId > 0) {
            uComponent.setAnchorId(anchorId);
        }
        Long behaviorId = userComponentRequest.getBehaviorId();
        if (behaviorId > 0) {
            uComponent.setBehaviorId(behaviorId);
        }
        Long celebrationId = userComponentRequest.getCelebrationId();
        if (celebrationId > 0) {
            uComponent.setCelebrationId(celebrationId);
        }
        UserComponent updatedUserComponent = userComponentRepository.save(uComponent);
        return ResponseEntity.ok(new UserComponentResponse(updatedUserComponent.getId(), updatedUserComponent.getComponent().getId(), updatedUserComponent.getUser().getId(), updatedUserComponent.getCreatedDate().getTime(), updatedUserComponent.isFinished(), updatedUserComponent.getStep(), updatedUserComponent.getAnchorId(), updatedUserComponent.getBehaviorId(), updatedUserComponent.getCelebrationId()));
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<Map<String, List<Map<String, Object>>>> getUserComponentsByUserId(@PathVariable Long userId) {
        Map<String, List<Map<String, Object>>> userComponents = userComponentService.getUserComponentsByUserId(userId);
        return ResponseEntity.ok(userComponents);
    }

}
