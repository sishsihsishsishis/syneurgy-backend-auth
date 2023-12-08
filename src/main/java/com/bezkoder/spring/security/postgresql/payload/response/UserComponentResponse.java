package com.bezkoder.spring.security.postgresql.payload.response;

import lombok.Getter;

public class UserComponentResponse {
    @Getter
    private Long id;
    @Getter
    private Long componentId;
    @Getter
    private Long userId;
    @Getter
    private long createdDate;
    @Getter
    private boolean isFinished;
    @Getter
    private int step;

    @Getter
    private Long anchorId;

    @Getter
    private Long behaviorId;

    @Getter
    private Long celebrationId;
    public UserComponentResponse(Long id, Long componentId, Long userId, long createdDate, boolean isFinished, int step, Long anchorId, Long behaviorId, Long celebrationId) {
        this.id = id;
        this.componentId = componentId;
        this.userId = userId;
        this.createdDate = createdDate;
        this.isFinished = isFinished;
        this.step = step;
        this.anchorId = anchorId;
        this.behaviorId = behaviorId;
        this.celebrationId = celebrationId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setComponentId(Long componentId) {
        this.componentId = componentId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }


    public void setStep(int step) {
        this.step = step;
    }

    public void setAnchorId(Long anchorId) {
        this.anchorId = anchorId;
    }

    public void setBehaviorId(Long behaviorId) {
        this.behaviorId = behaviorId;
    }

    public void setCelebrationId(Long celebrationId) {
        this.celebrationId = celebrationId;
    }
}

