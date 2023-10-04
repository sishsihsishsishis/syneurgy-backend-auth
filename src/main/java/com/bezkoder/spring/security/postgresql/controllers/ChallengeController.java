package com.bezkoder.spring.security.postgresql.controllers;

import com.bezkoder.spring.security.postgresql.models.Challenge;
import com.bezkoder.spring.security.postgresql.models.User;
import com.bezkoder.spring.security.postgresql.payload.response.MessageResponse;
import com.bezkoder.spring.security.postgresql.repository.ChallengeRepository;
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
@RequestMapping("/api/challenges")
@Api(tags = "Challenge")
public class ChallengeController {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    ChallengeRepository challengeRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping
    @ApiOperation("Get All Challenges, required user token")
    public ResponseEntity<?> getAllChallenges(@RequestParam(required = false) String search, @RequestHeader(name = "Authorization") String token) {
        String username = jwtUtils.getExistingUsername(token);
        Optional<User> existingUser = userRepository.findByUsername(username);

        if (!existingUser.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Error: The current user is not unavailable!"));
        }

        List<Challenge> challenges = new ArrayList<>();
        challengeRepository.findAllByOrderByIdAsc().forEach(challenges::add);
        if (search != null && !search.isEmpty()) {
            challenges = challenges.stream()
                    .filter(challenge -> challenge.getName().toLowerCase().contains(search.toLowerCase()))
                    .toList(); // Requires Java 16 or later; use toList() from Collectors in earlier versions
        }
        if (challenges.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(challenges, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ApiOperation("Get challenge by id. required user token")
    public ResponseEntity<?> getChallengeById(@PathVariable Long id, @RequestHeader(name = "Authorization") String token) {
        String username = jwtUtils.getExistingUsername(token);
        Optional<User> existingUser = userRepository.findByUsername(username);

        if (!existingUser.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Error: The current user is not unavailable!"));
        }

        Optional<Challenge> challenge = challengeRepository.findById(id);
        return challenge.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ApiOperation("Create Challenge")
    public ResponseEntity<?> createChallenge(@RequestBody Challenge challenge, @RequestHeader(name = "Authorization") String token) {
        String username = jwtUtils.getExistingUsername(token);
        Optional<User> existingUser = userRepository.findByUsername(username);

        if (!existingUser.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Error: The current user is not unavailable!"));
        }

        return ResponseEntity.ok(challengeRepository.save(challenge));
    }

    @PutMapping("/{id}")
    @ApiOperation("Update challenge by id")
    public ResponseEntity<?> updateChallenge(@PathVariable Long id, @RequestBody Challenge updatedChallenge, @RequestHeader(name = "Authorization") String token) {
        String username = jwtUtils.getExistingUsername(token);
        Optional<User> existingUser = userRepository.findByUsername(username);

        if (!existingUser.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Error: The current user is not unavailable!"));
        }

        Optional<Challenge> existingChallenge = challengeRepository.findById(id);
        if (existingChallenge.isPresent()) {
            updatedChallenge.setId(id);
            return ResponseEntity.ok(challengeRepository.save(updatedChallenge));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Delete Challenge By id")
    public ResponseEntity<?> deleteChallenge(@PathVariable Long id, @RequestHeader(name = "Authorization") String token) {
        String username = jwtUtils.getExistingUsername(token);
        Optional<User> existingUser = userRepository.findByUsername(username);

        if (!existingUser.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Error: The current user is not unavailable!"));
        }

        Optional<Challenge> challenge = challengeRepository.findById(id);
        if (challenge.isPresent()) {
            challengeRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
