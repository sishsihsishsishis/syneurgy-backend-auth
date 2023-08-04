package com.bezkoder.spring.security.postgresql.payload.request;

import javax.validation.constraints.NotBlank;

public class TeamRequest {
    @NotBlank
    private String teamName;

    private Integer step;

    private Long teamId;

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

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }
}
