package com.bezkoder.spring.security.postgresql.payload.response;

public class TeamInfoResponse {
    private String username;
    private String email;

    private String teamName;

    public TeamInfoResponse(String username, String email, String teamName) {
        this.username = username;
        this.email = email;
        this.teamName = teamName;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
}
