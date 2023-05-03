package com.bezkoder.spring.security.postgresql.payload.request;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Set;

public class InviteRequest {

    private List<String> emails;


    private Long teamId;

    private Long userId;

    private Integer step;

    public List<String> getEmails() {
        return emails;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
