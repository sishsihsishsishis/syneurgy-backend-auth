package com.bezkoder.spring.security.postgresql.service;

import com.bezkoder.spring.security.postgresql.dto.ConferenceDTO;
import com.bezkoder.spring.security.postgresql.models.Conference;
import com.bezkoder.spring.security.postgresql.repository.ComponentRepository;
import com.bezkoder.spring.security.postgresql.repository.ConferenceRepository;
import com.bezkoder.spring.security.postgresql.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ConferenceService {
    @Autowired
    private ConferenceRepository conferenceRepository;
    @Autowired
    private ComponentRepository componentRepository;

    @Autowired
    private UserRepository userRepository;

    public List<ConferenceDTO> getNextConferencesByUserComponentId(Long userComponentId) {
        Date nowDate = new Date();
        List<Conference> conferences = conferenceRepository.findByUserComponentNewIdAndConferenceStartTimeAfter(userComponentId, nowDate);
        return conferences.stream()
                .map(ConferenceDTO::new)
                .collect(Collectors.toList());
    }

    public Map<String, List<Long>> getConferenceCountsByWeek(int month, int year, Long userId) {
        List<Conference> conferences = conferenceRepository.findConferencesInMonthForUser(month, year, userId);

        // Organize conference counts by week
        Map<String, List<Long>> conferenceCountsByWeek = new LinkedHashMap<>();
        WeekFields weekFields = WeekFields.of(Locale.getDefault());

        for (Conference conference: conferences) {
            LocalDate conferenceDate = conference.getConferenceStartTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int weekIndex = conferenceDate.get(weekFields.weekOfMonth());

            String weekKey = "Week " + weekIndex;
            conferenceCountsByWeek.computeIfAbsent(weekKey, k -> new ArrayList<>()).add(conference.getId());
        }

        // Convert the List<Long> to List<Long> representing the count
        conferenceCountsByWeek.replaceAll((key, value) -> Collections.singletonList((long) value.size()));

        return conferenceCountsByWeek;
    }

    public int calculateConferencePercentageForUser(Long userId) {
        List<Conference> allConferences = conferenceRepository.findByUserId(userId);
        List<Conference> pastConferences = conferenceRepository.findPastConferencesByUserId(userId);

        int allConferencesCount = allConferences.size();
        int pastConferencesCount = pastConferences.size();

        if (allConferencesCount == 0) {
            return 0;
        }

        return (pastConferencesCount * 100) / allConferencesCount;
    }
}
