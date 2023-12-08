package com.bezkoder.spring.security.postgresql.payload.request;

public class UserComponentRequest {
    private Long anchorId;
    private Long behaviorId;
    private Long celebrationId;

    public void setAnchorId(Long anchorId) {
        this.anchorId = anchorId;
    }

    public Long getAnchorId() {
        return anchorId;
    }

    public void setBehaviorId(Long behaviorId) {
        this.behaviorId = behaviorId;
    }

    public Long getBehaviorId() {
        return behaviorId;
    }

    public void setCelebrationId(Long celebrationId) {
        this.celebrationId = celebrationId;
    }

    public Long getCelebrationId() {
        return celebrationId;
    }
}
