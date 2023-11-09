package com.bezkoder.spring.security.postgresql.controllers;

import com.bezkoder.spring.security.postgresql.models.Habit;
import com.bezkoder.spring.security.postgresql.models.User;
import com.bezkoder.spring.security.postgresql.models.UserChallenge;
import com.bezkoder.spring.security.postgresql.models.UserChallengeHabit;
import com.bezkoder.spring.security.postgresql.payload.request.UserChallengeHabitRequest;
import com.bezkoder.spring.security.postgresql.payload.response.MessageResponse;
import com.bezkoder.spring.security.postgresql.payload.response.UserChallengeHabitResponse;
import com.bezkoder.spring.security.postgresql.repository.HabitRepository;
import com.bezkoder.spring.security.postgresql.repository.UserChallengeHabitRepository;
import com.bezkoder.spring.security.postgresql.repository.UserChallengeRepository;
import com.bezkoder.spring.security.postgresql.repository.UserRepository;
import com.bezkoder.spring.security.postgresql.security.jwt.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user_challenge_habit")
@Api(tags = "User Challenge Habit")
public class UserChallengeHabitController {
    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserChallengeRepository userChallengeRepository;

    @Autowired
    UserChallengeHabitRepository userChallengeHabitRepository;

    @Autowired
    HabitRepository habitRepository;

    @PostMapping
    @ApiOperation("Create User Challenge Habit, required user token")
    public ResponseEntity<?> createUserChallengeHabit(@RequestBody UserChallengeHabitRequest userChallengeHabitRequest) {
        Long uChallenge_id = userChallengeHabitRequest.getuChallenge_id();

        Optional<UserChallenge> userChallenge = userChallengeRepository.findById(uChallenge_id);
        if (!userChallenge.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("The current user Challenge is not unavailable!"));
        }

        UserChallenge uChallenge = userChallenge.get();

        Long[] habit_ids = userChallengeHabitRequest.getHabit_ids();
        if (habit_ids.length == 0) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Please choose habits!"));
        }

        UserChallengeHabit userChallengeHabit = new UserChallengeHabit(uChallenge, habit_ids);
        UserChallengeHabit result = userChallengeHabitRepository.save(userChallengeHabit);

        return ResponseEntity.ok(new UserChallengeHabitResponse(result.getId(), result.getUserChallenge().getId(), result.getHabitIds(), "Success"));

    }

    @GetMapping("/by-user-challenge/{userChallengeId}")
    @ApiOperation("Get userChallengeHabit by userChallenge Id, required user token")
    public ResponseEntity<?> getUserChallengeHabitByuChallengeId(@PathVariable Long userChallengeId) {
        List<UserChallengeHabit> ucHabits = userChallengeHabitRepository.findByUserChallengeId(userChallengeId);
        List<UserChallengeHabitResponse> responses = new ArrayList<>();
        List<Habit> habits = new ArrayList<>();
        if (ucHabits.size() > 0) {
            UserChallengeHabit ucHabit = ucHabits.get(0);
            Long[] habit_ids = ucHabit.getHabitIds();
            habits = habitRepository.findAllById(Arrays.asList(habit_ids));
        }
        return new ResponseEntity<>(habits, HttpStatus.OK);
    }
}
