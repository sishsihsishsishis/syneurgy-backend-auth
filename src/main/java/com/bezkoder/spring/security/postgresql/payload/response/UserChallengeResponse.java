package com.bezkoder.spring.security.postgresql.payload.response;

public class UserChallengeResponse {
    private Long id;
    private Long challengeCategoryId;

    private Long userId;

    public UserChallengeResponse(Long id, Long challengeCategoryId, Long userId) {
        this.id = id;
        this.challengeCategoryId = challengeCategoryId;
        this.userId = userId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Long getChallengeCategoryId() {
        return challengeCategoryId;
    }

    public void setChallengeCategoryId(Long challengeCategoryId) {
        this.challengeCategoryId = challengeCategoryId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
