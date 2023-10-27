package com.bezkoder.spring.security.postgresql.controllers;


import com.bezkoder.spring.security.postgresql.models.*;
import com.bezkoder.spring.security.postgresql.payload.request.MeetingRequest;
import com.bezkoder.spring.security.postgresql.payload.request.TimeRange;
import com.bezkoder.spring.security.postgresql.payload.request.UserChallengeMeetingRequest;
import com.bezkoder.spring.security.postgresql.payload.response.MessageResponse;
import com.bezkoder.spring.security.postgresql.payload.response.TopPerformanceUserResponse;

import com.bezkoder.spring.security.postgresql.repository.MeetingRepository;
import com.bezkoder.spring.security.postgresql.repository.UserChallengeRepository;
import com.bezkoder.spring.security.postgresql.repository.UserRepository;
import com.bezkoder.spring.security.postgresql.security.jwt.JwtUtils;
import com.bezkoder.spring.security.postgresql.service.MeetingService;
import com.bezkoder.spring.security.postgresql.service.UserChallengeService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/behavior")
@Api(tags = "Behavior Engine")
public class BehaviorEngineController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    MeetingRepository meetingRepository;

    @Autowired
    UserChallengeRepository userChallengeRepository;
    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    private MeetingService meetingService;

    @Autowired
    private UserChallengeService userChallengeService;

    @PostMapping("/challenge_meetings")
    public ResponseEntity<?> postMeetingTimes(@Valid @RequestBody UserChallengeMeetingRequest userChallengeMeetingRequest) {

        Long uChallenge_id = userChallengeMeetingRequest.getUser_challenge_id();

        Optional<UserChallenge> userChallenge = userChallengeRepository.findById(uChallenge_id);
        if (!userChallenge.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: The current user Challenge is not unavailable!"));
        }

        UserChallenge uChallenge = userChallenge.get();

        MeetingRequest[] meetings = userChallengeMeetingRequest.getMeetings();

        if (meetings.length == 0) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Meeting Times are not selected"));
        }
        for (MeetingRequest meetingRequest: meetings) {
            TimeRange timeRange = meetingRequest.getTime();
            Long startTime = 0L;
            Long endTime = 0L;
            if (timeRange != null) {

                if(timeRange.isStartNull() == false) {
                    startTime = timeRange.getStart();
                }
                if (timeRange.isEndNull() == false) {
                    endTime = timeRange.getEnd();
                }
            }
            String meetingId = "";
            if (meetingRequest.isIdNull() == false) {
                meetingId = meetingRequest.getId();
            }
            String meetingTitle = meetingRequest.getTitle();
            Boolean isCustom = meetingRequest.getCustom();
            Long totalConcurrentEvents = meetingRequest.getTotalConcurrentEvents();
            int meetingReminderTime = meetingRequest.getMeetingReminderTime();
            Meeting meeting = new Meeting(uChallenge, startTime, endTime, meetingId, meetingTitle, isCustom, totalConcurrentEvents, meetingReminderTime);
            meetingRepository.save(meeting);
        }

        int percent = meetingService.calculateMeetingPercentageForUser(uChallenge.getUser().getId());
        uChallenge.setPercent(percent);
        userChallengeRepository.save(uChallenge);
        return ResponseEntity.ok("Meetings are anchored successfully");
    }

    @GetMapping("/topUsers")
    public ResponseEntity<?> getTopFiveUsersAndPercent() {
        List<UserChallenge> userChallenges = userChallengeService.getTopFiveUC();
        List<TopPerformanceUserResponse> topPerformanceUserResponses = new ArrayList<>();
        userChallenges.forEach(userChallenge -> {
            User user = userChallenge.getUser();
            TopPerformanceUserResponse topPerformanceUserResponse = new TopPerformanceUserResponse(user.getFullName(), user.getPhoto(), userChallenge.getPercent(), user.getId());
            topPerformanceUserResponses.add(topPerformanceUserResponse);
        });
        return ResponseEntity.ok(topPerformanceUserResponses);
    }
}
