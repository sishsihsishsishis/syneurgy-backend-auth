package com.bezkoder.spring.security.postgresql.controllers;

import com.bezkoder.spring.security.postgresql.dto.ConferenceDTO;
import com.bezkoder.spring.security.postgresql.models.*;
import com.bezkoder.spring.security.postgresql.payload.request.ReminderTimeRequest;
import com.bezkoder.spring.security.postgresql.payload.response.MessageResponse;
import com.bezkoder.spring.security.postgresql.repository.ConferenceRepository;
import com.bezkoder.spring.security.postgresql.repository.UserComponentRepository;
import com.bezkoder.spring.security.postgresql.repository.UserRepository;
import com.bezkoder.spring.security.postgresql.security.jwt.JwtUtils;
import com.bezkoder.spring.security.postgresql.service.ConferenceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/conferences")
@Api(tags = "Conference")
public class ConferenceController {
    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserComponentRepository userComponentRepository;

    @Autowired
    ConferenceRepository conferenceRepository;
    @Autowired
    private ConferenceService conferenceService;

    @GetMapping("/userComponent/{userComponentId}")
    public ResponseEntity<List<ConferenceDTO>> getConferencesByUserComponentId(@PathVariable Long userComponentId) {
        List<ConferenceDTO> conferenceDTOS = conferenceService.getNextConferencesByUserComponentId(userComponentId);
        return new ResponseEntity<>(conferenceDTOS, HttpStatus.OK);
    }

    @GetMapping("/myNextConferences")
    @ApiOperation("Get Conferences by UserId, required user token")
    public ResponseEntity<?> getConferencesByUserId(@RequestHeader(name = "Authorization") String token) {
        String username = jwtUtils.getExistingUsername(token);
        Optional<User> existingUser = userRepository.findByUsername(username);

        if (existingUser.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("The current user is not unavailable!"));
        }

        User user = existingUser.get();
        List<ConferenceDTO> results = new ArrayList<>();
        List<UserComponent> userComponents = userComponentRepository.findByUserId(user.getId());
        userComponents.forEach(userComponent -> {
            List<ConferenceDTO> conferenceDTOS = conferenceService.getNextConferencesByUserComponentId(userComponent.getId());
            if (!conferenceDTOS.isEmpty()) {
                results.addAll(conferenceDTOS);
            }
        });

        results.forEach(conferenceDTO -> {
            conferenceDTO.getConferenceStartTimeTimestamp();
            conferenceDTO.getConferenceEndTimeTimestamp();
            conferenceDTO.getCreatedDateTimestamp();
        });

        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @GetMapping("/participationLevel/{currentDate}")
    @ApiOperation("Get conferences count for previous month and current month")
    public ResponseEntity<?> getConferencesCountByWeekForTwoMonths(@PathVariable Long currentDate, @RequestHeader(name = "Authorization") String token) {
        String username = jwtUtils.getExistingUsername(token);
        Optional<User> existingUser = userRepository.findByUsername(username);

        if (existingUser.isEmpty()) {
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

        Map<String, List<Long>> currentMonthResult = conferenceService.getConferenceCountsByWeek(monthValue, year, currentUser.getId());
        Map<String, List<Long>> previousMonthResult = conferenceService.getConferenceCountsByWeek(previousMonth, previousYear, currentUser.getId());

        Map<String, Map<String, List<Long>>> response = new HashMap<>();
        response.put("currentMonth", currentMonthResult);
        response.put("previousMonth", previousMonthResult);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/percentage")
    @ApiOperation("Get percentage of past conferences")
    public ResponseEntity<?> getPercentageOfConferences(@RequestHeader(name = "Authorization") String token) {
        String username = jwtUtils.getExistingUsername(token);
        Optional<User> existingUser = userRepository.findByUsername(username);

        if (existingUser.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("The current user is not unavailable!"));
        }

        User currentUser = existingUser.get();
        List<UserComponent> userComponents = userComponentRepository.findByUserIdOrderByCreatedDateDesc(currentUser.getId());
        int percentage = 0;
        if (userComponents.size() > 0) {
            UserComponent userComponent = userComponents.get(0);
            percentage = userComponent.getUser().getPercent();
        }

        return ResponseEntity.ok(percentage);
    }

    @PostMapping("/reminderConference")
    @ApiOperation("set reminder time")
    public ResponseEntity<?> setReminderTime(@Valid @RequestBody ReminderTimeRequest reminderTimeRequest) {
        Long[] conferenceIds = reminderTimeRequest.getConference_ids();
        Long reminderTime = reminderTimeRequest.getReminderTime();
        List<Conference> conferences = conferenceRepository.findByIdIn(conferenceIds);

        for (Conference conference: conferences) {
            conference.setConferenceReminderTime(reminderTime.intValue());
        }

        conferenceRepository.saveAll(conferences);
        return ResponseEntity.ok("Updated successfully");
    }

}
