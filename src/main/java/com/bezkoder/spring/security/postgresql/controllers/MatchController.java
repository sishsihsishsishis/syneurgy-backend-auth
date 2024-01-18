package com.bezkoder.spring.security.postgresql.controllers;

import com.bezkoder.spring.security.postgresql.models.MatchEntity;
import com.bezkoder.spring.security.postgresql.payload.request.MatchInfo;
import com.bezkoder.spring.security.postgresql.payload.request.MatchInfoRequest;
import com.bezkoder.spring.security.postgresql.payload.request.MatchUserIdInfo;
import com.bezkoder.spring.security.postgresql.payload.request.MatchUserIdRequest;
import com.bezkoder.spring.security.postgresql.payload.response.MatchResponse;
import com.bezkoder.spring.security.postgresql.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/match")
public class MatchController {
    @Autowired
    MatchRepository matchRepository;

    @PostMapping("/fillInfo")
    public Map<String, Object> fillInfo(@RequestBody MatchInfoRequest request) {
        List<MatchInfo> infoList = request.getInfo();
        Long meetingId = request.getMeetingId();
        Date currentDate = new Date();

        // Create a list to store the results
        List<MatchEntity> resultEntities = new ArrayList<>();

        for (MatchInfo matchInfo : infoList) {
            MatchEntity matchEntity = new MatchEntity();
            matchEntity.setUsername(matchInfo.getUsername());
            matchEntity.setSpeaker(matchInfo.getSpeaker());
            matchEntity.setMeetingId(meetingId);
            matchEntity.setCreatedDate(currentDate);
            matchEntity.setUpdatedDate(currentDate);

            // Save the entity to the database
            matchEntity = matchRepository.save(matchEntity);

            // Add the saved entity to the result list
            resultEntities.add(matchEntity);
        }

        Map<String, Object> response = new HashMap<>();
        if (!resultEntities.isEmpty()) {
            List<MatchResponse> matchResponses = MatchResponse.listFromEntities(resultEntities);
            response.put("status", "success");
            response.put("message", "Records retrieved successfully");
            response.put("records", matchResponses);
        } else {
            response.put("status", "error");
            response.put("message", "No records found for meetingId: " + meetingId);
        }

        return response;
    }

    @PostMapping("/fillUserId")
    public Map<String, Object> fillUserId(@RequestBody MatchUserIdRequest request) {
        List<MatchUserIdInfo> infoList = request.getInfo();
        Long meetingId = request.getMeetingId();
        Date updatedDate = new Date();

        List<MatchEntity> resultEntities = new ArrayList<>();
        for (MatchUserIdInfo userIdInfo : infoList) {
            // Retrieve the existing entity from the database using username and meetingId
            MatchEntity existingEntity = matchRepository.findByUsernameAndMeetingId(userIdInfo.getUsername(), meetingId);

            if (existingEntity != null) {
                // Update the entity with userId and updatedDate
                existingEntity.setUserId(userIdInfo.getUserId());
                existingEntity.setUpdatedDate(updatedDate);

                // Save the updated entity to the database
                existingEntity = matchRepository.save(existingEntity);

                // Add the updated entity to the result list
                resultEntities.add(existingEntity);
            }
        }

        Map<String, Object> response = new HashMap<>();
        if (!resultEntities.isEmpty()) {
            List<MatchResponse> matchResponses = MatchResponse.listFromEntities(resultEntities);
            response.put("status", "success");
            response.put("message", "Records retrieved successfully");
            response.put("records", matchResponses);
        } else {
            response.put("status", "error");
            response.put("message", "No records found for meetingId: " + meetingId);
        }

        return response;

    }

    @GetMapping("/getFinalRecord/{userId}")
    public Map<String, Object> getFinalRecordByUserId(@PathVariable Long userId) {
        Map<String, Object> response = new HashMap<>();

        // Retrieve the final record by userId and latest updatedDate
        Optional<MatchEntity> optionalFinalRecord = matchRepository.findTopByUserIdOrderByUpdatedDateDesc(userId);

        if (optionalFinalRecord.isPresent()) {
            MatchEntity finalRecord = optionalFinalRecord.get();
            MatchResponse matchResponse = MatchResponse.convertMatchEntity(finalRecord);
            response.put("status", "success");
            response.put("message", "Final record retrieved successfully");
            response.put("finalRecord", matchResponse);
        } else {
            response.put("status", "error");
            response.put("message", "Final record not found for userId: " + userId);
        }

        return response;
    }

    @GetMapping("/getMatchesByMeetingId/{meetingId}")
    public Map<String, Object> getMatchesByMeetingId(@PathVariable Long meetingId) {
        Map<String, Object> response = new HashMap<>();

        // Retrieve records by meetingId
        List<MatchEntity> records = matchRepository.findByMeetingId(meetingId);

        if (!records.isEmpty()) {
            List<MatchResponse> matchResponses = MatchResponse.listFromEntities(records);
            response.put("status", "success");
            response.put("message", "Records retrieved successfully");
            response.put("records", matchResponses);
        } else {
            response.put("status", "error");
            response.put("message", "No records found for meetingId: " + meetingId);
        }

        return response;
    }
}
