package com.bezkoder.spring.security.postgresql.controllers;

import com.bezkoder.spring.security.postgresql.models.MeetingMinutes;
import com.bezkoder.spring.security.postgresql.models.User;
import com.bezkoder.spring.security.postgresql.models.UserMinutes;
import com.bezkoder.spring.security.postgresql.payload.request.InitializeMeetingMinutesRequest;
import com.bezkoder.spring.security.postgresql.payload.request.MeetingMinutesRequest;
import com.bezkoder.spring.security.postgresql.payload.response.MessageResponse;
import com.bezkoder.spring.security.postgresql.repository.MeetingMinutesRepository;
import com.bezkoder.spring.security.postgresql.repository.UserMinutesRepository;
import com.bezkoder.spring.security.postgresql.repository.UserRepository;
import com.bezkoder.spring.security.postgresql.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/meetings")
public class MeetingMinutesController {

    @Autowired
    private MeetingMinutesRepository meetingMinutesRepository;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserMinutesRepository userMinutesRepository;

    @PostMapping("/initialize-meeting-minutes")
    public ResponseEntity<?> initializeMeetingMinutes(@RequestBody InitializeMeetingMinutesRequest request) {
        // Check if a record with userId and meetingId already exists
        Optional<MeetingMinutes> existingMeetingMinutes = meetingMinutesRepository.findByUserIdAndMeetingId(request.getUserId(), request.getMeetingId());

        if (!existingMeetingMinutes.isPresent()) {
            // If the record doesn't exist, create a new one with meeting_minutes set to 0
            MeetingMinutes meetingMinutes = new MeetingMinutes();
            meetingMinutes.setUserId(request.getUserId());
            meetingMinutes.setMeetingId(request.getMeetingId());
            meetingMinutes.setMeetingMinutes(0);

            // Save the new record in the database
            meetingMinutesRepository.save(meetingMinutes);

            return ResponseEntity.ok("Meeting minutes initialized successfully.");
        } else {
            // If the record exists, do nothing
            return ResponseEntity.ok("Meeting minutes already exist for this user and meeting.");
        }
    }
    @PostMapping("/minutes")
    public ResponseEntity<?> saveMeetingMinutes(@RequestBody MeetingMinutesRequest request, @RequestHeader(name = "Authorization") String token) {
        String username = jwtUtils.getExistingUsername(token);
        Optional<User> existingUser1 = userRepository.findByUsername(username);

        if (!existingUser1.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("The current user is unavailable!"));
        }

        List<MeetingMinutes> updatedMeetingMinutesList = new ArrayList<>();

        // Iterate over each meeting in the request
        for (MeetingMinutesRequest.Meeting meeting : request.getMeetings()) {
            // Check if a record with userId and meetingId exists
            Optional<MeetingMinutes> existingMeetingMinutes = meetingMinutesRepository.findByUserIdAndMeetingId(request.getUserId(), meeting.getMeetingId());

            if (existingMeetingMinutes.isPresent()) {
                // If the record exists, update the meeting minutes
                MeetingMinutes meetingMinutes = existingMeetingMinutes.get();
                meetingMinutes.setMeetingMinutes(meeting.getMinutes());
                updatedMeetingMinutesList.add(meetingMinutes);
            }
        }

        // Save the updated meeting minutes
        if (!updatedMeetingMinutesList.isEmpty()) {
            meetingMinutesRepository.saveAll(updatedMeetingMinutesList);
        }

        // Step 2: Calculate total minutes consumed by the user
        Integer totalConsumedMinutes = meetingMinutesRepository.sumMeetingMinutesByUserId(request.getUserId());
        Integer totalConsumedMinutesSafe = totalConsumedMinutes != null ? totalConsumedMinutes : 0;
        // Step 3: Update the consumed_minutes in user_minutes table
        Optional<UserMinutes> userMinutes1 = userMinutesRepository.findByUserId(request.getUserId());
        if (!userMinutes1.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("There is no user minutes."));
        }
        UserMinutes userMinutes = userMinutes1.get();
        userMinutes.setConsumedMinutes(totalConsumedMinutesSafe);
        userMinutesRepository.save(userMinutes);
        // Step 4: Return the updated user_minutes details
        Map<String, Object> response = new HashMap<>();
        response.put("user_id", userMinutes.getUserId());
        response.put("all_minutes", userMinutes.getAllMinutes());
        response.put("consumed_minutes", userMinutes.getConsumedMinutes());

        return ResponseEntity.ok(response);
    }
}