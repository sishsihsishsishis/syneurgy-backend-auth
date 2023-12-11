package com.bezkoder.spring.security.postgresql.service;

import com.bezkoder.spring.security.postgresql.models.Conference;
import com.bezkoder.spring.security.postgresql.models.UserComponent;
import com.bezkoder.spring.security.postgresql.repository.ConferenceRepository;
import com.postmarkapp.postmark.Postmark;
import com.postmarkapp.postmark.client.ApiClient;
import com.postmarkapp.postmark.client.data.model.message.Message;
import com.postmarkapp.postmark.client.exception.PostmarkException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
@Service
public class ConferenceSchedulerService {
    @Autowired
    ConferenceRepository conferenceRepository;

    @Value("${meeting_reminder_time}")
    private int defaultMeetingReminderTime;

    @Scheduled(fixedRate = 30000) // Run every 30 seconds (adjust as needed)
    public void checkUpcomingConferences() {
        // Add logic to check and notify users about upcoming conferences
        // This method will be executed at the specified interval
        // You can inject necessary services or components here

        Date nowDate = new Date();
        List<Conference> conferenceList = conferenceRepository.findByConferenceStartTimeAfterAndIsSentIsFalse(nowDate);

        LocalDateTime now = LocalDateTime.now();

        if (!conferenceList.isEmpty()) {
            for (int i = 0; i < conferenceList.size(); i++) {
                Conference existingConference = conferenceList.get(i);
                LocalDateTime conferenceTime = DateToLocalDateTimeConverter.convertDateToLocalDateTime(existingConference.getConferenceStartTime());
                long mins = Duration.between(now, conferenceTime).toMinutes();
                int conferenceReminderTime = existingConference.getConferenceReminderTime();
                if (conferenceReminderTime == 0) {
                    conferenceReminderTime = defaultMeetingReminderTime;
                }
                if (mins == conferenceReminderTime) {
                    UserComponent userComponent = existingConference.getUserComponent();
                    String userEmail = userComponent.getUser().getEmail();
                    String userName = userComponent.getUser().getFullName();
                    ApiClient client = Postmark.getApiClient("2274a4ca-df74-4850-8b4c-06d1da6c14a2");

                    Message message = new Message("notifications@syneurgy.com", userEmail, "It's time for your habit.", "Hi, " + userName + ". You have a conference in " + defaultMeetingReminderTime +"minutes. Please prepare something to grow your habits." + existingConference.getConferenceId() );

                    try {
                        com.postmarkapp.postmark.client.data.model.message.MessageResponse response = client.deliverMessage(message);
                    } catch (PostmarkException | IOException e) {
                        throw new RuntimeException(e);
                    }
                    existingConference.setSent(true);
                    conferenceRepository.save(existingConference);
                    conferenceList.set(i, existingConference);
                }
            }


        }

    }
}
