package com.bezkoder.spring.security.postgresql.payload.request;

public class UserChallengeMeetingRequest {
    private Long user_challenge_id;
    private MeetingRequest[] meetings;

    public void setUser_challenge_id(Long user_challenge_id) {
        this.user_challenge_id = user_challenge_id;
    }

    public Long getUser_challenge_id() {
        return user_challenge_id;
    }

    public MeetingRequest[] getMeetings() {
        return meetings;
    }

    public void setMeetings(MeetingRequest[] meetings) {
        this.meetings = meetings;
    }
}
