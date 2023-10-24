package com.bezkoder.spring.security.postgresql.controllers;

import com.bezkoder.spring.security.postgresql.models.GCParam;
import com.bezkoder.spring.security.postgresql.models.User;
import com.bezkoder.spring.security.postgresql.payload.request.GCRequest;
import com.bezkoder.spring.security.postgresql.payload.response.MessageResponse;
import com.bezkoder.spring.security.postgresql.repository.GCParamRepository;
import com.bezkoder.spring.security.postgresql.repository.UserRepository;
import com.bezkoder.spring.security.postgresql.security.jwt.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/google")
@Api(tags = "Google")
public class GoogleController {
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    UserRepository userRepository;
    @Autowired
    GCParamRepository gcParamRepository;
    @GetMapping
    public ResponseEntity<?> getGoogleParams(@RequestHeader(name = "Authorization") String token) {
        String username = jwtUtils.getExistingUsername(token);
        Optional<User> existingUser = userRepository.findByUsername(username);

        if (!existingUser.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: The current user is not unavailable!"));
        }

        User currentUser = existingUser.get();
        List<GCParam> gcParamList = gcParamRepository.findByUserId(currentUser.getId());
        if (gcParamList.size() > 0) {
            GCParam gcParam = gcParamList.get(0);
            return new ResponseEntity<>(gcParam, HttpStatus.OK);
        }

        return ResponseEntity.ok("No content");
    }

    @PostMapping
    public ResponseEntity<?> addGoogleParams(@Valid @RequestBody GCRequest gcRequest, @RequestHeader(name = "Authorization") String token) {
        String username = jwtUtils.getExistingUsername(token);
        Optional<User> existingUser = userRepository.findByUsername(username);

        if (!existingUser.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: The current user is not unavailable!"));
        }

        User currentUser = existingUser.get();
        List<GCParam> gcParamList = gcParamRepository.findByUserId(currentUser.getId());

        GCParam newGCParam;
        GCParam gcParam;
        if (gcParamList.size() > 0) {
            gcParam = gcParamList.get(0);
            gcParam.setCode(gcRequest.getCode());
            gcParam.setClient_id(gcRequest.getClient_id());
            gcParam.setClient_secret(gcRequest.getClient_secret());
            gcParam.setRedirect_uri(gcRequest.getRedirect_uri());
            gcParam.setGrant_type(gcRequest.getGrant_type());
        } else {
            gcParam = new GCParam(gcRequest.getCode(), gcRequest.getClient_id(), gcRequest.getClient_secret(), gcRequest.getRedirect_uri(), gcRequest.getGrant_type(), currentUser.getId());
        }
        newGCParam = gcParamRepository.save(gcParam);

        return new ResponseEntity<>(newGCParam, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getGoogleParamsWithId(@PathVariable Long id, @RequestHeader(name = "Authorization") String token) {
        String username = jwtUtils.getExistingUsername(token);
        Optional<User> existingUser = userRepository.findByUsername(username);

        if (!existingUser.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Error: The current user is not unavailable!"));
        }

        Optional<GCParam> gcParam = gcParamRepository.findById(id);
        return gcParam.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateGoogleParamsWithId(@PathVariable Long id, @Valid @RequestBody GCRequest gcRequest, @RequestHeader(name = "Authorization") String token) {
        String username = jwtUtils.getExistingUsername(token);
        Optional<User> existingUser = userRepository.findByUsername(username);

        if (!existingUser.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Error: The current user is not unavailable!"));
        }
        Optional<GCParam> gcParam = gcParamRepository.findById(id);

        if (gcParam.isPresent()) {
            GCParam currentGCParam = gcParam.get();
            if (gcRequest.getCode() != null && gcRequest.getCode().length() > 0) {
                currentGCParam.setCode(gcRequest.getCode());
            }
            if (gcRequest.getClient_id() != null && gcRequest.getClient_id().length() > 0) {
                currentGCParam.setClient_id(gcRequest.getClient_id());
            }
            if (gcRequest.getClient_secret() != null && gcRequest.getClient_secret().length() > 0) {
                currentGCParam.setClient_secret(gcRequest.getClient_secret());
            }
            if (gcRequest.getRedirect_uri() != null && gcRequest.getRedirect_uri().length() > 0) {
                currentGCParam.setRedirect_uri(gcRequest.getRedirect_uri());
            }
            if (gcRequest.getGrant_type() != null && gcRequest.getGrant_type().length() > 0) {
                currentGCParam.setGrant_type(gcRequest.getGrant_type());
            }
            return ResponseEntity.ok(gcParamRepository.save(currentGCParam));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGoogleParamsWithId(@PathVariable Long id, @RequestHeader(name = "Authorization") String token) {
        String username = jwtUtils.getExistingUsername(token);
        Optional<User> existingUser = userRepository.findByUsername(username);

        if (!existingUser.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Error: The current user is not unavailable!"));
        }
        Optional<GCParam> gcParam = gcParamRepository.findById(id);

        if (gcParam.isPresent()) {
            gcParamRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }

    }
}
