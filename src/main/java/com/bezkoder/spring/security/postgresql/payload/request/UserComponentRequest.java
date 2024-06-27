package com.bezkoder.spring.security.postgresql.payload.request;

public class UserComponentRequest {
    private Long contextId;
    private Long behaviorId;


    private Long componentId;

    public void setContextId(Long contextId) {
        this.contextId = contextId;
    }

    public Long getContextId() {
        return contextId;
    }

    public void setBehaviorId(Long behaviorId) {
        this.behaviorId = behaviorId;
    }

    public Long getBehaviorId() {
        return behaviorId;
    }


    public Long getComponentId() {
        return componentId;
    }

    public void setComponentId(Long componentId) {
        this.componentId = componentId;
    }
}
