package com.bezkoder.spring.security.postgresql.payload.request;

public class ChallengeRequest {
    private Long id;
    private String name;
    private String description;

    private Long uChallengeId;

    private Long[] meetingTimes;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public Long getuChallengeId() {
        return uChallengeId;
    }

    public void setuChallengeId(Long uChallengeId) {
        this.uChallengeId = uChallengeId;
    }

    public Long[] getMeetingTimes() {
        return meetingTimes;
    }

    public void setMeetingTimes(Long[] meetingTimes) {
        this.meetingTimes = meetingTimes;
    }
}
