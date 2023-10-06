package com.bezkoder.spring.security.postgresql.controllers;


import com.bezkoder.spring.security.postgresql.models.Challenge;
import com.bezkoder.spring.security.postgresql.models.Meeting;
import com.bezkoder.spring.security.postgresql.models.User;
import com.bezkoder.spring.security.postgresql.models.UserChallenge;
import com.bezkoder.spring.security.postgresql.payload.request.ChallengeRequest;
import com.bezkoder.spring.security.postgresql.payload.response.MessageResponse;
import com.bezkoder.spring.security.postgresql.payload.response.UserChallengeResponse;
import com.bezkoder.spring.security.postgresql.repository.ChallengeRepository;
import com.bezkoder.spring.security.postgresql.repository.MeetingRepository;
import com.bezkoder.spring.security.postgresql.repository.UserChallengeRepository;
import com.bezkoder.spring.security.postgresql.repository.UserRepository;
import com.bezkoder.spring.security.postgresql.security.jwt.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/behavior")
@Api(tags = "Behavior Engine")
public class BehaviorEngineController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MeetingRepository meetingRepository;
    @Autowired
    ChallengeRepository challengeRepository;

    @Autowired
    UserChallengeRepository userChallengeRepository;
    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/challenge")
    @ApiOperation("Create new challenge")
    public ResponseEntity<?> createBEWizard(@RequestHeader(name = "Authorization") String token) {
        String username = jwtUtils.getExistingUsername(token);
        Optional<User> existingUser1 = userRepository.findByUsername(username);

        if (!existingUser1.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: The current user is not unavailable!"));
        }
        User currentUser = existingUser1.get();

        List<Challenge> challengeList = challengeRepository.findAllByOrderByIdAsc();

        if(challengeList.size() == 0) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("The challenges are not unavailable!"));
        }
        Challenge firstChallenge = challengeList.get(0);
        UserChallenge userChallenge = new UserChallenge(currentUser, firstChallenge);
        UserChallenge newUserChallenge = userChallengeRepository.save(userChallenge);
        return ResponseEntity.ok(new UserChallengeResponse(newUserChallenge.getId(), newUserChallenge.getChallenge().getId(), newUserChallenge.getUser().getId()));
    }

    @PutMapping("/challenge/{id}")
    @ApiOperation("Update Challenge By id")
    public ResponseEntity<?> updateChallenge(@PathVariable Long id, @RequestBody ChallengeRequest challengeRequest, @RequestHeader(name = "Authorization") String token) {
        String username = jwtUtils.getExistingUsername(token);
        Optional<User> existingUser = userRepository.findByUsername(username);

        if (!existingUser.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: The current user is not unavailable!"));
        }

        Optional<UserChallenge> userChallenge = userChallengeRepository.findById(id);
        if (!userChallenge.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: The current user Challenge is not unavailable!"));
        }

        UserChallenge uChallenge = userChallenge.get();
        Long newId = challengeRequest.getId();
        Optional<Challenge> challenge = challengeRepository.findById(newId);
        if(!challenge.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("The current challenge is not unavailable!"));
        }
        Challenge challenge1 = challenge.get();
        uChallenge.setChallenge(challenge1);

        UserChallenge updatedUserChallenge = userChallengeRepository.save(uChallenge);
        return ResponseEntity.ok(new UserChallengeResponse(updatedUserChallenge.getId(), updatedUserChallenge.getChallenge().getId(), updatedUserChallenge.getUser().getId()));

    }

    @PostMapping("/challenge_meetings")
    public ResponseEntity<?> postMeetingTimes(@Valid @RequestBody ChallengeRequest challengeRequest, @RequestHeader(name = "Authorization") String token) {
        String username = jwtUtils.getExistingUsername(token);
        Optional<User> existingUser = userRepository.findByUsername(username);

        if (!existingUser.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: The current user is not unavailable!"));
        }

        Long uChallenge_id = challengeRequest.getuChallengeId();

        Optional<UserChallenge> userChallenge = userChallengeRepository.findById(uChallenge_id);
        if (!userChallenge.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: The current user Challenge is not unavailable!"));
        }

        UserChallenge uChallenge = userChallenge.get();

        Long[] meetingTimes = challengeRequest.getMeetingTimes();
        if (meetingTimes.length == 0) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Meeting Times are not selected"));
        }
        for (Long meetingTime: meetingTimes) {
            Meeting meeting = new Meeting(uChallenge, meetingTime);
            meetingRepository.save(meeting);
        }

        return ResponseEntity.ok("Meetings are anchored successfully");
    }
}
