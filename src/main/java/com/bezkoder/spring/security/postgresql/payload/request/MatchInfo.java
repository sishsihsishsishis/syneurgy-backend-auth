package com.bezkoder.spring.security.postgresql.payload.request;

public class MatchInfo {
    private String username;
    private String speaker;

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }
}
