package com.bezkoder.spring.security.postgresql.payload.response;

public class TeamResponse {

    private Long id;

    private String teamName;

    private Integer step;

    public TeamResponse(Long id, String teamName, Integer step) {
        this.id = id;
        this.teamName = teamName;
        this.step = step;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

}
