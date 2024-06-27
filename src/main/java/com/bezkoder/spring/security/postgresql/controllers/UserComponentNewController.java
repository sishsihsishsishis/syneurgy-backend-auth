package com.bezkoder.spring.security.postgresql.controllers;

import com.bezkoder.spring.security.postgresql.exception.UserComponentNewNotFoundException;
import com.bezkoder.spring.security.postgresql.models.ComponentNew;
import com.bezkoder.spring.security.postgresql.models.User;
import com.bezkoder.spring.security.postgresql.models.UserComponentNew;
import com.bezkoder.spring.security.postgresql.payload.request.ComponentRequest;
import com.bezkoder.spring.security.postgresql.payload.request.UserComponentRequest;
import com.bezkoder.spring.security.postgresql.payload.response.MessageResponse;
import com.bezkoder.spring.security.postgresql.payload.response.UserComponentNewResponse;
import com.bezkoder.spring.security.postgresql.repository.ComponentNewRepository;
import com.bezkoder.spring.security.postgresql.repository.UserComponentNewRepository;
import com.bezkoder.spring.security.postgresql.repository.UserRepository;
import com.bezkoder.spring.security.postgresql.security.jwt.JwtUtils;
import com.bezkoder.spring.security.postgresql.service.UserComponentNewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/u_component_new")
@Api(tags = "User Component New")
public class UserComponentNewController {
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ComponentNewRepository componentNewRepository;

    @Autowired
    UserComponentNewRepository userComponentNewRepository;

    @Autowired
    UserComponentNewService userComponentNewService;
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
        List<ComponentNew> componentNewList = componentNewRepository.findAllByOrderByIdAsc();

        if (componentNewList.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("The components are unavailable!"));
        }
        ComponentNew firstComponent = componentNewList.get(0);
        UserComponentNew userComponentNew = new UserComponentNew(currentUser, firstComponent);
        UserComponentNew userComponentNew1 = userComponentNewRepository.save(userComponentNew);
        return ResponseEntity.ok(new UserComponentNewResponse(userComponentNew1.getId(), userComponentNew1.getComponentNew().getId(), userComponentNew1.getUser().getId(), userComponentNew1.getCreatedDate().getTime(), userComponentNew1.isFinished(), userComponentNew1.getStep(), userComponentNew1.getContextId(), userComponentNew1.getBehaviorId()));
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

        Optional<ComponentNew> componentNew = componentNewRepository.findById(componentId);
        if (componentNew.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("The current component is unavailable!"));
        }
        ComponentNew existingComponentNew = componentNew.get();
        UserComponentNew userComponentNew = new UserComponentNew(currentUser, existingComponentNew);
        UserComponentNew userComponentNew1 = userComponentNewRepository.save(userComponentNew);
        return ResponseEntity.ok(new UserComponentNewResponse(userComponentNew1.getId(), userComponentNew1.getComponentNew().getId(), userComponentNew1.getUser().getId(), userComponentNew1.getCreatedDate().getTime(), userComponentNew1.isFinished(), userComponentNew1.getStep(), userComponentNew1.getContextId(), userComponentNew1.getBehaviorId()));
    }

    @PutMapping("/{id}")
    @ApiOperation("Update User Component By id")
    public ResponseEntity<?> updateComponent(@PathVariable Long id, @RequestBody ComponentRequest componentRequest) {
        Optional<UserComponentNew> userComponentNew = userComponentNewRepository.findById(id);
        if (userComponentNew.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("The current user component is unavailable!"));
        }
        UserComponentNew userComponentNew1 = userComponentNew.get();
        Long newId = componentRequest.getId();
        Optional<ComponentNew> componentNew = componentNewRepository.findById(newId);
        if(componentNew.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("The current component is unavailable!"));
        }
        ComponentNew componentNew1 = componentNew.get();
        userComponentNew1.setComponentNew(componentNew1);

        UserComponentNew updatedUserComponentNew = userComponentNewRepository.save(userComponentNew1);
        return ResponseEntity.ok(new UserComponentNewResponse(updatedUserComponentNew.getId(), updatedUserComponentNew.getComponentNew().getId(), updatedUserComponentNew.getUser().getId(), updatedUserComponentNew.getCreatedDate().getTime(), updatedUserComponentNew.isFinished(), updatedUserComponentNew.getStep(), updatedUserComponentNew.getContextId(), updatedUserComponentNew.getBehaviorId()));
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

        List<UserComponentNew> componentNews = userComponentNewRepository.findUnfinishedUserComponentNewsByUserId(currentUser.getId());
        if (componentNews.isEmpty()) {
            return ResponseEntity.ok("There's nothing");

        }
        UserComponentNew componentNew = componentNews.get(0);
        return ResponseEntity.ok(new UserComponentNewResponse(componentNew.getId(), componentNew.getComponentNew().getId(), componentNew.getUser().getId(), componentNew.getCreatedDate().getTime(), componentNew.isFinished(), componentNew.getStep(), componentNew.getContextId(), componentNew.getBehaviorId()));
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<Object>> getUserComponentsByUserId(@PathVariable Long userId) {
        List<Object> userComponents = userComponentNewService.getUserComponentsByUserId(userId);
        return ResponseEntity.ok(userComponents);
    }

    @PutMapping("/mark-as-finished/{userComponentId}")
    public ResponseEntity<?> markComponentAsFinished(@PathVariable Long userComponentId) {
        try {
            UserComponentNew userComponentNew = userComponentNewService.markComponentAsFinished(userComponentId);
            return ResponseEntity.ok(new UserComponentNewResponse(userComponentNew.getId(), userComponentNew.getComponentNew().getId(), userComponentNew.getUser().getId(), userComponentNew.getCreatedDate().getTime(), userComponentNew.isFinished(), userComponentNew.getStep(), userComponentNew.getContextId(), userComponentNew.getBehaviorId()));
        } catch (UserComponentNewNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update-step/{userComponentId}")
    public ResponseEntity<?> updateStep(@PathVariable Long userComponentId, @RequestParam int newStep) {
        try {
            UserComponentNew userComponentNew = userComponentNewService.updateStep(userComponentId, newStep);
            return ResponseEntity.ok(new UserComponentNewResponse(userComponentNew.getId(), userComponentNew.getComponentNew().getId(), userComponentNew.getUser().getId(), userComponentNew.getCreatedDate().getTime(), userComponentNew.isFinished(), userComponentNew.getStep(), userComponentNew.getContextId(), userComponentNew.getBehaviorId()));
        } catch (UserComponentNewNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update/{userComponentId}")
    public ResponseEntity<?> updateUserComponent(@PathVariable Long userComponentId, @RequestBody UserComponentRequest userComponentRequest) {
        Optional<UserComponentNew> userComponentNew = userComponentNewRepository.findById(userComponentId);
        if (userComponentNew.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("The current user component is unavailable!"));
        }

        UserComponentNew uComponent = userComponentNew.get();
        UserComponentNew userComponentNew1 = getUserComponent(userComponentRequest, uComponent);
        UserComponentNew userComponentNew2 = userComponentNewRepository.save(userComponentNew1);
        return ResponseEntity.ok(new UserComponentNewResponse(userComponentNew2.getId(), userComponentNew2.getComponentNew().getId(), userComponentNew2.getUser().getId(), userComponentNew2.getCreatedDate().getTime(), userComponentNew2.isFinished(), userComponentNew2.getStep(), userComponentNew2.getContextId(), userComponentNew2.getBehaviorId()));
    }

    private static UserComponentNew getUserComponent(UserComponentRequest userComponentRequest, UserComponentNew userComponent) {

        Long contextId = userComponentRequest.getContextId();
        if (contextId > 0) {
            userComponent.setContextId(contextId);
        }
        Long behaviorId = userComponentRequest.getBehaviorId();
        if (behaviorId > 0) {
            userComponent.setBehaviorId(behaviorId);

        }
        return userComponent;
    }
}
