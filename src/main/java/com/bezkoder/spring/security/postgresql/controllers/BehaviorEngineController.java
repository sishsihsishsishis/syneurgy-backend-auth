package com.bezkoder.spring.security.postgresql.controllers;


import com.bezkoder.spring.security.postgresql.models.*;
import com.bezkoder.spring.security.postgresql.payload.request.AuthRequest;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

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

    @Value("${client_id}")
    private String clientId;

    @Value("${client_secret}")
    private String clientSecret;

    @Value("${redirect_uri}")
    private String redirectUri;

    @Value("${grant_type}")
    private String grantType;

    @PostMapping("/challenge_meetings")
    public ResponseEntity<?> postMeetingTimes(@Valid @RequestBody UserChallengeMeetingRequest userChallengeMeetingRequest, @RequestHeader(name = "Authorization") String token) {

        String username = jwtUtils.getExistingUsername(token);
        Optional<User> existingUser = userRepository.findByUsername(username);

        if (!existingUser.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: The current user is not unavailable!"));
        }

        User currentUser = existingUser.get();

        Long uChallenge_id = userChallengeMeetingRequest.getUser_challenge_id();

        Optional<UserChallenge> userChallenge = userChallengeRepository.findById(uChallenge_id);
        if (!userChallenge.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: The current user Challenge is not unavailable!"));
        }

        UserChallenge uChallenge = userChallenge.get();
        if (currentUser.getId() != uChallenge.getUser().getId()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: The current user Challenge is not unavailable!"));
        }

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
        User user = uChallenge.getUser();
        user.setPercent(percent);
        uChallenge.setUser(userRepository.save(user));
        userChallengeRepository.save(uChallenge);
        return ResponseEntity.ok("Meetings are anchored successfully");
    }

    @GetMapping("/topUsers")
    public ResponseEntity<?> getTopFiveUsersAndPercent() {
        List<TopPerformanceUserResponse> topPerformanceUserResponses = new ArrayList<>();
        List<User> users = userRepository.findTop5ByFirstNameIsNotNullOrderByPercentDesc();
        users.forEach(user -> {
            TopPerformanceUserResponse topPerformanceUserResponse = new TopPerformanceUserResponse(user.getFullName(), user.getPhoto(), user.getPercent(), user.getId());
            topPerformanceUserResponses.add(topPerformanceUserResponse);
        });

        return ResponseEntity.ok(topPerformanceUserResponses);
    }

    @PostMapping("/GoogleToken")
    public ResponseEntity<String> exchangeToken(@RequestBody AuthRequest authRequest) {
        String tokenUrl = "https://oauth2.googleapis.com/token";

        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("code", authRequest.getCode().trim());
        data.add("client_id", clientId);
        data.add("client_secret", clientSecret);
        data.add("redirect_uri", redirectUri);
        data.add("grant_type", grantType);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(data, headers);

        try {
            // Use WebClient instead of RestTemplate in newer versions of Spring
            ResponseEntity<String> response = new RestTemplate().postForEntity(tokenUrl, request, String.class);

            // Log request and response details for debugging
            System.out.println("Request: " + request);
            System.out.println("Response: " + response);

            // Process the response, save tokens, and return a suitable response to the frontend.
            return response;
        } catch (HttpClientErrorException.BadRequest badRequestException) {
            // Log additional details for Bad Request exception
            System.err.println("Bad Request Exception Details: " + badRequestException.getResponseBodyAsString());
            throw badRequestException;
        }
    }

    @PostMapping("/GoogleRefreshToken")
    public ResponseEntity<String> refreshToken(@RequestBody AuthRequest authRequest) {
        String tokenUrl = "https://oauth2.googleapis.com/token";

        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("grant_type", "refresh_token");
        data.add("refresh_token", authRequest.getRefresh_token());
        data.add("client_id", clientId);
        data.add("client_secret", clientSecret);
        data.add("redirect_uri", redirectUri);


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(data, headers);

        try {
            // Use WebClient instead of RestTemplate in newer versions of Spring
            ResponseEntity<String> response = new RestTemplate().postForEntity(tokenUrl, request, String.class);

            // Log request and response details for debugging
            System.out.println("Request: " + request);
            System.out.println("Response: " + response);

            // Process the response, save tokens, and return a suitable response to the frontend.
            return response;
        } catch (HttpClientErrorException.BadRequest badRequestException) {
            // Log additional details for Bad Request exception
            System.err.println("Bad Request Exception Details: " + badRequestException.getResponseBodyAsString());
            throw badRequestException;
        }
    }
}
