package com.bezkoder.spring.security.postgresql.controllers;

import com.bezkoder.spring.security.postgresql.models.*;
import com.bezkoder.spring.security.postgresql.payload.request.*;
import com.bezkoder.spring.security.postgresql.payload.response.MessageResponse;
import com.bezkoder.spring.security.postgresql.payload.response.TokenResponse;
import com.bezkoder.spring.security.postgresql.payload.response.TopPerformanceUserResponse;
import com.bezkoder.spring.security.postgresql.repository.ConferenceRepository;
import com.bezkoder.spring.security.postgresql.repository.UserComponentNewRepository;
import com.bezkoder.spring.security.postgresql.repository.UserRepository;
import com.bezkoder.spring.security.postgresql.security.jwt.JwtUtils;
import com.bezkoder.spring.security.postgresql.service.ConferenceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
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
@RequestMapping("/api/engine")
@Api(tags = "Engine")
public class BEController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    ConferenceRepository conferenceRepository;

    @Autowired
    UserComponentNewRepository userComponentNewRepository;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    private ConferenceService conferenceService;

    @Value("${client_id}")
    private String clientId;

    @Value("${client_secret}")
    private String clientSecret;

    @Value("${redirect_uri}")
    private String redirectUri;

    @Value("${grant_type}")
    private String grantType;

    @PostMapping("/component_conferences")
    public ResponseEntity<?> postConferenceTimes(@Valid @RequestBody UserComponentConferenceRequest userComponentConferenceRequest, @RequestHeader(name = "Authorization") String token) {
        String username = jwtUtils.getExistingUsername(token);
        Optional<User> existingUser = userRepository.findByUsername(username);

        if (!existingUser.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("The current user is unavailable!"));
        }

        User currentUser = existingUser.get();

        Long uComponent_id = userComponentConferenceRequest.getUser_component_id();

        Optional<UserComponentNew> userComponentNew = userComponentNewRepository.findById(uComponent_id);

        if (userComponentNew.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("The current user Component is unavailable!"));
        }

        UserComponentNew uComponent = userComponentNew.get();
        if (currentUser.getId() != uComponent.getUser().getId()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("The current user Component is unavailable!"));
        }

        ConferenceRequest[] conferences = userComponentConferenceRequest.getConferences();
        List<Long> conferenceIds = new ArrayList<>();
        if (conferences.length == 0) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Conference Times are not selected"));
        }
        for (ConferenceRequest conferenceRequest: conferences) {
            TimeRange timeRange = conferenceRequest.getTime();
            Long startTime = 0L;
            Long endTime = 0L;
            if (timeRange != null) {

                if(!timeRange.isStartNull()) {
                    startTime = timeRange.getStart();
                }
                if (!timeRange.isEndNull()) {
                    endTime = timeRange.getEnd();
                }
            }
            String conferenceId = "";
            if (!conferenceRequest.isIdNull()) {
                conferenceId = conferenceRequest.getId();
            }
            String conferenceTitle = conferenceRequest.getTitle();
            Boolean isCustom = conferenceRequest.getCustom();
            Long totalConcurrentEvents = conferenceRequest.getTotalConcurrentEvents();
            int conferenceReminderTime = conferenceRequest.getConferenceReminderTime();
            Conference conference = new Conference(uComponent, startTime, endTime, conferenceId, conferenceTitle, isCustom, totalConcurrentEvents, conferenceReminderTime);
            Conference conferenceResult = conferenceRepository.save(conference);
            conferenceIds.add(conferenceResult.getId());
        }

        int percent = conferenceService.calculateConferencePercentageForUser(uComponent.getUser().getId());
        User user = uComponent.getUser();
        user.setPercent(percent);
        uComponent.setUser(userRepository.save(user));
        userComponentNewRepository.save(uComponent);
        return new ResponseEntity<>(conferenceIds, HttpStatus.OK);
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
    public ResponseEntity<TokenResponse> exchangeToken(@RequestBody AuthRequest authRequest) {
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

            if (response.getStatusCode() == HttpStatus.OK) {
                // Parse response body
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode responseBody = objectMapper.readTree(response.getBody());

                // Extract fields from response
                String accessToken = responseBody.get("access_token").asText();
                int expiresIn = responseBody.get("expires_in").asInt();
                String refreshToken = responseBody.get("refresh_token").asText();

                // Create custom response object
                TokenResponse tokenResponse = new TokenResponse(accessToken, expiresIn, refreshToken);

                // Return response entity
                return ResponseEntity.ok(tokenResponse);
            } else {
                // Handle error cases (e.g., return an error response)
                return ResponseEntity.status(response.getStatusCode()).body(null);
            }

            // Process the response, save tokens, and return a suitable response to the frontend.

        } catch (HttpClientErrorException.BadRequest badRequestException) {
            // Log additional details for Bad Request exception
            System.err.println("Bad Request Exception Details: " + badRequestException.getResponseBodyAsString());
            throw badRequestException;
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/GoogleRefreshToken")
    public ResponseEntity<TokenResponse> refreshToken(@RequestBody AuthRequest authRequest) {
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
            System.out.println("Response111: " + response);

            // Process the response, save tokens, and return a suitable response to the frontend.
            if (response.getStatusCode() == HttpStatus.OK) {
                // Parse response body
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode responseBody = objectMapper.readTree(response.getBody());

                // Extract fields from response
                String accessToken = responseBody.get("access_token").asText();
                int expiresIn = responseBody.get("expires_in").asInt();


                // Create custom response object
                TokenResponse tokenResponse = new TokenResponse(accessToken, expiresIn, null);

                // Return response entity
                return ResponseEntity.ok(tokenResponse);
            } else {
                // Handle error cases (e.g., return an error response)
                return ResponseEntity.status(response.getStatusCode()).body(null);
            }
        } catch (HttpClientErrorException.BadRequest badRequestException) {
            // Log additional details for Bad Request exception
            System.err.println("Bad Request Exception Details: " + badRequestException.getResponseBodyAsString());
            throw badRequestException;
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
