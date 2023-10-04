package com.bezkoder.spring.security.postgresql.payload.response;

public class UserChallengeResponse {
    private Long id;
    private Long challengeId;

    private Long userId;

    public UserChallengeResponse(Long id, Long challengeId, Long userId) {
        this.id = id;
        this.challengeId = challengeId;
        this.userId = userId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Long getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(Long challengeId) {
        this.challengeId = challengeId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
