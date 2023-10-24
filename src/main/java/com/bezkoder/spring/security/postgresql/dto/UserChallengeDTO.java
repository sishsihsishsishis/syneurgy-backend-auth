package com.bezkoder.spring.security.postgresql.dto;

import com.bezkoder.spring.security.postgresql.models.Challenge;
import com.bezkoder.spring.security.postgresql.models.User;
import com.bezkoder.spring.security.postgresql.models.UserChallenge;

public class UserChallengeDTO {
    private Long id;
    private User user;

    private Challenge challenge;

    public UserChallengeDTO(UserChallenge userChallenge) {
        this.id = userChallenge.getId();
        this.user = userChallenge.getUser();
        this.challenge = userChallenge.getChallenge();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Challenge getChallenge() {
        return challenge;
    }

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }
}