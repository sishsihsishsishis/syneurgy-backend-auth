package com.bezkoder.spring.security.postgresql.service;

import com.bezkoder.spring.security.postgresql.models.Meeting;
import com.bezkoder.spring.security.postgresql.models.UserChallenge;
import com.bezkoder.spring.security.postgresql.repository.MeetingRepository;
import com.postmarkapp.postmark.Postmark;
import com.postmarkapp.postmark.client.ApiClient;
import com.postmarkapp.postmark.client.data.model.message.Message;
import com.postmarkapp.postmark.client.data.model.message.MessageResponse;
import com.postmarkapp.postmark.client.data.model.templates.TemplatedMessage;
import com.postmarkapp.postmark.client.exception.PostmarkException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class MeetingSchedulerService {

    @Autowired
    MeetingRepository meetingRepository;
    @Scheduled(fixedRate = 30000) // Run every 30 seconds (adjust as needed)
    public void checkUpcomingMeetings() {
        // Add logic to check and notify users about upcoming meetings
        // This method will be executed at the specified interval
        // You can inject necessary services or components here

        Date nowDate = new Date();
        List<Meeting> meetingList = meetingRepository.findByMeetingStartTimeAfterAndIsSentIsFalse(nowDate);

        LocalDateTime now = LocalDateTime.now();

        if (meetingList.size() > 0) {
            for (int i = 0; i < meetingList.size(); i++) {
                Meeting existingMeeting = meetingList.get(i);
                LocalDateTime meetingTime = DateToLocalDateTimeConverter.convertDateToLocalDateTime(existingMeeting.getMeetingStartTime());
                long mins = Duration.between(now, meetingTime).toMinutes();
                if (mins == 30) {
                    UserChallenge userChallenge = existingMeeting.getUserChallenge();
                    String userEmail = userChallenge.getUser().getEmail();
                    String userName = userChallenge.getUser().getFullName();
                    ApiClient client = Postmark.getApiClient("2274a4ca-df74-4850-8b4c-06d1da6c14a2");

                    Message message = new Message("notifications@syneurgy.com", userEmail, "It's time for your habit.", "Hi, " + userName + ". You have meetings in 30minutes. Please prepare something to grow your habits." );

                    try {
                        com.postmarkapp.postmark.client.data.model.message.MessageResponse response = client.deliverMessage(message);
                    } catch (PostmarkException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    existingMeeting.setSent(true);
                    meetingRepository.save(existingMeeting);
                    meetingList.set(i, existingMeeting);
                }
            }


        }

    }
}