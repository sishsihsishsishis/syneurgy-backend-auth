package com.bezkoder.spring.security.postgresql.service;

import com.bezkoder.spring.security.postgresql.dto.HabitDTO;
import com.bezkoder.spring.security.postgresql.dto.MeetingDTO;
import com.bezkoder.spring.security.postgresql.models.Habit;
import com.bezkoder.spring.security.postgresql.models.Meeting;
import com.bezkoder.spring.security.postgresql.models.UserChallengeHabit;
import com.bezkoder.spring.security.postgresql.repository.HabitRepository;
import com.bezkoder.spring.security.postgresql.repository.MeetingRepository;
import com.bezkoder.spring.security.postgresql.repository.UserChallengeHabitRepository;
import com.bezkoder.spring.security.postgresql.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MeetingService {
    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private HabitRepository habitRepository;

    @Autowired
    private UserChallengeHabitRepository userChallengeHabitRepository;

    @Autowired
    private UserRepository userRepository;

    public List<MeetingDTO> getNextMeetingsByUserChallengeId(Long userChallengeId) {
        Date nowDate = new Date();
        List<Meeting> meetings = meetingRepository.findByUserChallengeIdAndMeetingStartTimeAfter(userChallengeId, nowDate);
        if (meetings.size() == 0) {
            Habit habit = new Habit();
            habit.setId(-1L);
            habit.setCategory(0L);
            habit.setDescription("");
            habit.setTitle("");
            return meetings.stream()
                    .map(meeting -> new MeetingDTO(meeting, new HabitDTO(habit)))
                    .collect(Collectors.toList());
        }
        List<UserChallengeHabit> userChallengeHabits = userChallengeHabitRepository.findByUserChallengeId(userChallengeId);
        if (userChallengeHabits.size() == 0) {
            Habit habit = new Habit();
            habit.setId(-1L);
            habit.setCategory(0L);
            habit.setDescription("");
            habit.setTitle("");
            return meetings.stream()
                    .map(meeting -> new MeetingDTO(meeting, new HabitDTO(habit)))
                    .collect(Collectors.toList());
        }
        UserChallengeHabit userChallengeHabit = userChallengeHabits.get(0);
        return meetings.stream()
                .map(meeting -> new MeetingDTO(meeting, getFirstHabitDetails(userChallengeHabit.getHabitIds())))
                .collect(Collectors.toList());
    }

    private HabitDTO getFirstHabitDetails(Long[] habitIds) {
        if (habitIds != null && habitIds.length > 0) {
            Long firstHabitId = habitIds[0];
            // Use HabitRepository to get details by ID
            Optional<Habit> habitOptional = habitRepository.findById(firstHabitId);
            return habitOptional.map(HabitDTO::new).orElse(null);
        }
        return null;
    }

    public Map<String, List<Long>> getMeetingCountsByWeek(int month, int year, Long userId) {
        List<Meeting> meetings = meetingRepository.findMeetingsInMonthForUser(month, year, userId);

        // Organize meeting counts by week
        Map<String, List<Long>> meetingCountsByWeek = new LinkedHashMap<>();
        WeekFields weekFields = WeekFields.of(Locale.getDefault());

        for (Meeting meeting : meetings) {
            LocalDate meetingDate = meeting.getMeetingStartTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int weekIndex = meetingDate.get(weekFields.weekOfMonth());

            String weekKey = "week " + weekIndex;
            meetingCountsByWeek.computeIfAbsent(weekKey, k -> new ArrayList<>()).add(meeting.getId()); // Counting meetings by ID

        }

        // Convert the List<Long> to List<Long> representing the count
        meetingCountsByWeek.replaceAll((key, value) -> Collections.singletonList((long) value.size()));

        return meetingCountsByWeek;
    }

    public int calculateMeetingPercentageForUser(Long userId) {
        List<Meeting> allMeetings = meetingRepository.findByUserId(userId);
        List<Meeting> pastMeetings = meetingRepository.findPastMeetingsByUserId(userId);

        int allMeetingsCount = allMeetings.size();
        int pastMeetingsCount = pastMeetings.size();

        if (allMeetingsCount == 0) {
            return 0; // Avoid division by zero
        }

        return (pastMeetingsCount * 100) / allMeetingsCount;
    }

}