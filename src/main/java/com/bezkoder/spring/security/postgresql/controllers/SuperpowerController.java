package com.bezkoder.spring.security.postgresql.controllers;

import com.bezkoder.spring.security.postgresql.models.Superpower;
import com.bezkoder.spring.security.postgresql.models.User;
import com.bezkoder.spring.security.postgresql.payload.response.MessageResponse;
import com.bezkoder.spring.security.postgresql.repository.SuperpowerRepository;
import com.bezkoder.spring.security.postgresql.repository.UserRepository;
import com.bezkoder.spring.security.postgresql.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/superpowers")
@Api(tags = "Superpower")
public class SuperpowerController {

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    SuperpowerRepository superpowerRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping
    @ApiOperation("Get all superpowers, required user token")
    public ResponseEntity<?> getAllSuperpowers(@RequestHeader(name = "Authorization") String token) {
        String username = jwtUtils.getExistingUsername(token);
        Optional<User> existingUser = userRepository.findByUsername(username);

        if (!existingUser.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Error: The current user is not unavailable!"));
        }

        List<Superpower> superpowers = new ArrayList<>();
        superpowerRepository.findAllByOrderByIdDesc().forEach(superpowers::add);
        if (superpowers.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(superpowers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ApiOperation("Get superpower by id, required user token")
    public ResponseEntity<?> getSuperpowerById(@PathVariable Long id, @RequestHeader(name = "Authorization") String token) {
        String username = jwtUtils.getExistingUsername(token);
        Optional<User> existingUser = userRepository.findByUsername(username);

        if (!existingUser.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Error: The current user is not unavailable!"));
        }

        Optional<Superpower> superpower = superpowerRepository.findById(id);
        return superpower.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ApiOperation("Create superpower, required user token")
    public ResponseEntity<?> createSuperpower(@RequestBody Superpower superpower, @RequestHeader(name = "Authorization") String token) {
        String username = jwtUtils.getExistingUsername(token);
        Optional<User> existingUser = userRepository.findByUsername(username);

        if (!existingUser.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Error: The current user is not unavailable!"));
        }

        return ResponseEntity.ok(superpowerRepository.save(superpower));
    }

    @PutMapping("/{id}")
    @ApiOperation("Update superpower by id, required user token")
    public ResponseEntity<?> updateSuperpower(@PathVariable Long id, @RequestBody Superpower updatedSuperpower, @RequestHeader(name = "Authorization") String token) {
        String username = jwtUtils.getExistingUsername(token);
        Optional<User> existingUser = userRepository.findByUsername(username);

        if (!existingUser.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Error: The current user is not unavailable!"));
        }

        Optional<Superpower> existingSuperpower = superpowerRepository.findById(id);
        if (existingSuperpower.isPresent()) {
            updatedSuperpower.setId(id);
            return ResponseEntity.ok(superpowerRepository.save(updatedSuperpower));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Delete superpower by id, required user token")
    public ResponseEntity<?> deleteSuperpower(@PathVariable Long id, @RequestHeader(name = "Authorization") String token) {
        String username = jwtUtils.getExistingUsername(token);
        Optional<User> existingUser = userRepository.findByUsername(username);

        if (!existingUser.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Error: The current user is not unavailable!"));
        }

        Optional<Superpower> superpower = superpowerRepository.findById(id);
        if (superpower.isPresent()) {
            superpowerRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
