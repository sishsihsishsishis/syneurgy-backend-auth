package com.bezkoder.spring.security.postgresql.payload.response;

import java.util.Date;

public class UserChallengeResponse {
    private Long id;
    private Long challengeCategoryId;

    private Long userId;

    private long createdDate;

    private boolean isFinished;

    private int step;

    public UserChallengeResponse(Long id, Long challengeCategoryId, Long userId, long createdDate, boolean isFinished, int step) {
        this.id = id;
        this.challengeCategoryId = challengeCategoryId;
        this.userId = userId;
        this.createdDate = createdDate;
        this.isFinished = isFinished;
        this.step = step;
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


    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public int getStep() {
        return step;
    }
}
