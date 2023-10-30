package com.bezkoder.spring.security.postgresql.controllers;

import com.bezkoder.spring.security.postgresql.dto.MeetingDTO;
import com.bezkoder.spring.security.postgresql.models.User;
import com.bezkoder.spring.security.postgresql.models.UserChallenge;
import com.bezkoder.spring.security.postgresql.payload.response.MessageResponse;
import com.bezkoder.spring.security.postgresql.repository.MeetingRepository;
import com.bezkoder.spring.security.postgresql.repository.UserChallengeRepository;
import com.bezkoder.spring.security.postgresql.repository.UserRepository;
import com.bezkoder.spring.security.postgresql.security.jwt.JwtUtils;
import com.bezkoder.spring.security.postgresql.service.MeetingService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.ApiOperation;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/meetings")
@Api(tags = "Meeting")
public class MeetingController {

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserChallengeRepository userChallengeRepository;

    @Autowired
    MeetingRepository meetingRepository;

    @Autowired
    private MeetingService meetingService;

    @GetMapping("/userChallenge/{userChallengeId}")
    public ResponseEntity<List<MeetingDTO>> getMeetingsByUserChallengeId(@PathVariable Long userChallengeId) {
        List<MeetingDTO> meetings = meetingService.getNextMeetingsByUserChallengeId(userChallengeId);
        return new ResponseEntity<>(meetings, HttpStatus.OK);
    }

    @GetMapping("/myNextMeetings")
    @ApiOperation("Get Meetings by UserId, required user token")
    public ResponseEntity<?> getMeetingsByUserId(@RequestHeader(name = "Authorization") String token) {
        String username = jwtUtils.getExistingUsername(token);
        Optional<User> existingUser = userRepository.findByUsername(username);

        if (!existingUser.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("The current user is not unavailable!"));
        }

        User user = existingUser.get();
        List<MeetingDTO> results = new ArrayList<>();
        List<UserChallenge> userChallenges = userChallengeRepository.findByUserId(user.getId());
        userChallenges.forEach(userChallenge -> {
            List<MeetingDTO> meetings = meetingService.getNextMeetingsByUserChallengeId(userChallenge.getId());
            if (meetings.size() > 0) {
                results.addAll(meetings);
            }

        });

        // Convert Date to timestamp for meetingStartTime
        results.forEach(meetingDTO -> {
            meetingDTO.getMeetingStartTimeTimestamp();
            meetingDTO.getMeetingEndTimeTimestamp();
            meetingDTO.getCreatedDateTimestamp();
        });
        return new ResponseEntity<>(results, HttpStatus.OK);

    }

    @GetMapping("/participationLevel/{currentDate}")
    @ApiOperation("Get meetings count for previous month and current month")
    public ResponseEntity<?> getMeetingsCountByWeekForTwoMonths(@PathVariable Long currentDate, @RequestHeader(name = "Authorization") String token) {
        String username = jwtUtils.getExistingUsername(token);
        Optional<User> existingUser = userRepository.findByUsername(username);

        if (!existingUser.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("The current user is not unavailable!"));
        }

        User currentUser = existingUser.get();
        Date date = new Date(currentDate);

        LocalDate localDate = date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();

        // Get the year and month separately
        int year = localDate.getYear();
        int monthValue = localDate.getMonthValue();

        // Calculate the date of the previous month
        LocalDate previousMonthDate = localDate.minusMonths(1);

        // Get the year and month of the previous month
        int previousYear = previousMonthDate.getYear();
        int previousMonth = previousMonthDate.getMonthValue();

        Map<String, List<Long>> currentMonthResult = meetingService.getMeetingCountsByWeek(monthValue, year, currentUser.getId());
        Map<String, List<Long>> previousMonthResult = meetingService.getMeetingCountsByWeek(previousMonth, previousYear, currentUser.getId());

        Map<String, Map<String, List<Long>>> response = new HashMap<>();
        response.put("currentMonth", currentMonthResult);
        response.put("previousMonth", previousMonthResult);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/percentage")
    @ApiOperation("Get percentage of past meetings")
    public ResponseEntity<?> getPercentageOfMeetings(@RequestHeader(name = "Authorization") String token) {
        String username = jwtUtils.getExistingUsername(token);
        Optional<User> existingUser = userRepository.findByUsername(username);

        if (!existingUser.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("The current user is not unavailable!"));
        }

        User currentUser = existingUser.get();
        List<UserChallenge> userChallenges = userChallengeRepository.findByUserIdOrderByCreatedDateDesc(currentUser.getId());
        int percentage = 0;
        if (userChallenges.size() > 0) {
            UserChallenge userChallenge = userChallenges.get(0);
            percentage = userChallenge.getUser().getPercent();
        }

        return ResponseEntity.ok(percentage);
    }

}
