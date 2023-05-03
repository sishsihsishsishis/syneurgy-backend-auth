package com.bezkoder.spring.security.postgresql.payload.request;

public class ConfirmInviteRequest {

    private  long teamId;
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setTeamId(long teamId) {
        this.teamId = teamId;
    }

    public long getTeamId() {
        return teamId;
    }
}
