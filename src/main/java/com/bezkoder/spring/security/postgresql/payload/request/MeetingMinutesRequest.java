package com.bezkoder.spring.security.postgresql.payload.request;

import java.util.List;

public class MeetingMinutesRequest {

    private Long userId;
    private List<Meeting> meetings;

    // Getters and Setters

    public static class Meeting {
        private Long meetingId;
        private Integer minutes;

        // Getters and Setters
        public Long getMeetingId() {
            return meetingId;
        }

        public void setMeetingId(Long meetingId) {
            this.meetingId = meetingId;
        }

        public Integer getMinutes() {
            return minutes;
        }

        public void setMinutes(Integer minutes) {
            this.minutes = minutes;
        }
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<Meeting> getMeetings() {
        return meetings;
    }

    public void setMeetings(List<Meeting> meetings) {
        this.meetings = meetings;
    }
}