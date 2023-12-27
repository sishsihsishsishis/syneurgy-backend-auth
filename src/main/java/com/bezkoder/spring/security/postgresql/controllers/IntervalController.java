package com.bezkoder.spring.security.postgresql.controllers;

import com.bezkoder.spring.security.postgresql.models.User;
import com.bezkoder.spring.security.postgresql.payload.response.MessageResponse;
import com.bezkoder.spring.security.postgresql.repository.ConferenceRepository;
import com.bezkoder.spring.security.postgresql.repository.UserRepository;
import com.bezkoder.spring.security.postgresql.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/interval")
public class IntervalController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ConferenceRepository conferenceRepository;

    @Autowired
    private EmailService emailService;
    @GetMapping("/finished-processing")
    public ResponseEntity<?> finishedProcessing(@RequestParam Long userId, @RequestParam Long meetingId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        User existingUser = user.get();

        emailService.sendSimpleEmail(existingUser.getEmail(), "Processing is finished", "Hello " + existingUser.getFullName() + "\n" + "Your meeting is finished processing. Please explore.");

        return ResponseEntity.ok(new MessageResponse("Email is sent successfully"));
    }
}
