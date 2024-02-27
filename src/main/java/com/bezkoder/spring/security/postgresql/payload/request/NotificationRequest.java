package com.bezkoder.spring.security.postgresql.payload.request;

public class NotificationRequest {
    private Long objId;
    private int type;
    private Long userId;

    public void setObjId(Long objId) {
        this.objId = objId;
    }

    public Long getObjId() {
        return objId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

}
