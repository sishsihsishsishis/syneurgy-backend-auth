package com.bezkoder.spring.security.postgresql.payload.response;

import lombok.Getter;

public class UserComponentNewResponse {

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
    private Long contextId;

    @Getter
    private Long behaviorId;

    public UserComponentNewResponse(Long id, Long componentId, Long userId, long createdDate, boolean isFinished, int step, Long contextId, Long behaviorId) {
        this.id = id;
        this.componentId = componentId;
        this.userId = userId;
        this.createdDate = createdDate;
        this.isFinished = isFinished;
        this.step = step;
        this.contextId = contextId;
        this.behaviorId = behaviorId;

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

    public void setContextId(Long contextId) {
        this.contextId = contextId;
    }
    public void setBehaviorId(Long behaviorId) {
        this.behaviorId = behaviorId;
    }

}
