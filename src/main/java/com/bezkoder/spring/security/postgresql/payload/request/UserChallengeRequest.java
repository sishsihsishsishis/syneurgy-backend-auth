package com.bezkoder.spring.security.postgresql.payload.request;

public class UserChallengeRequest {
    private Long user_challenge_id;
    private TimeRange[] meetingTimes;

    public void setUser_challenge_id(Long user_challenge_id) {
        this.user_challenge_id = user_challenge_id;
    }

    public Long getUser_challenge_id() {
        return user_challenge_id;
    }

    public TimeRange[] getMeetingTimes() {
        return meetingTimes;
    }

    public void setMeetingTimes(TimeRange[] meetingTimes) {
        this.meetingTimes = meetingTimes;
    }
}
