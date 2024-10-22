package com.bezkoder.spring.security.postgresql.controllers;


import com.bezkoder.spring.security.postgresql.models.User;
import com.bezkoder.spring.security.postgresql.models.UserAnswer;
import com.bezkoder.spring.security.postgresql.payload.response.MessageResponse;
import com.bezkoder.spring.security.postgresql.repository.UserRepository;
import com.bezkoder.spring.security.postgresql.security.jwt.JwtUtils;
import com.bezkoder.spring.security.postgresql.service.UserAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user-answers")
public class UserAnswerController {

    @Autowired
    private UserAnswerService userAnswerService;
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    UserRepository userRepository;

    // Endpoint to save a list of user answers
    @PostMapping("/save")
    public ResponseEntity<?> saveUserAnswers(@RequestBody List<UserAnswer> userAnswers, @RequestHeader(name = "Authorization") String token) {
        String username = jwtUtils.getExistingUsername(token);
        Optional<User> existingUser1 = userRepository.findByUsername(username);

        if (!existingUser1.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("The current user is unavailable!"));
        }
        User currentUser = existingUser1.get();
        Long currentUserId = currentUser.getId();
        if (!userAnswers.isEmpty()) {
            // Get the userId of the first object in the list
            Long firstUserId = userAnswers.get(0).getUserId();
            // You can now use the firstUserId as needed
            if (currentUserId != null && !currentUserId.equals(firstUserId)) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("User Id is incorrect!"));
            }
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Input value is wrong!"));
        }

        List<UserAnswer> savedAnswers = userAnswerService.saveAllUserAnswers(userAnswers);
        return ResponseEntity.ok(savedAnswers);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateUserAnswers(@RequestBody List<UserAnswer> userAnswers, @RequestHeader(name = "Authorization") String token) {
        String username = jwtUtils.getExistingUsername(token);
        Optional<User> existingUser1 = userRepository.findByUsername(username);

        if (!existingUser1.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("The current user is unavailable!"));
        }
        User currentUser = existingUser1.get();
        Long currentUserId = currentUser.getId();
        if (!userAnswers.isEmpty()) {
            // Get the userId of the first object in the list
            Long firstUserId = userAnswers.get(0).getUserId();
            // You can now use the firstUserId as needed
            if (currentUserId != null && !currentUserId.equals(firstUserId)) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("User Id is incorrect!"));
            }
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Input value is wrong!"));
        }

        // Update the list of user answers
        List<UserAnswer> updatedAnswers = userAnswerService.saveAllUserAnswers(userAnswers);
        return ResponseEntity.ok(updatedAnswers);
    }

    @GetMapping("/user")
    public ResponseEntity<?> getAnswersByUserId(@RequestHeader(name = "Authorization") String token) {
        String username = jwtUtils.getExistingUsername(token);
        Optional<User> existingUser1 = userRepository.findByUsername(username);

        if (!existingUser1.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("The current user is unavailable!"));
        }
        User currentUser = existingUser1.get();
        Long currentUserId = currentUser.getId();
        List<UserAnswer> answers = userAnswerService.getAnswersByUserId(currentUserId);
        return ResponseEntity.ok(answers);
    }
}