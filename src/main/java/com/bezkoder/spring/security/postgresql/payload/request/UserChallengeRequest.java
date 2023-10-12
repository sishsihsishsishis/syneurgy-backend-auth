package com.bezkoder.spring.security.postgresql.payload.request;

public class UserChallengeRequest {
    private Long id;
    private Long[] meetingTimes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long[] getMeetingTimes() {
        return meetingTimes;
    }

    public void setMeetingTimes(Long[] meetingTimes) {
        this.meetingTimes = meetingTimes;
    }
}
