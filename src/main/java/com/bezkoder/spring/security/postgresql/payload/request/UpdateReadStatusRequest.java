package com.bezkoder.spring.security.postgresql.payload.request;

import java.util.List;

public class UpdateReadStatusRequest {
    private List<Long> notificationIds;
    private boolean readStatus;

    public boolean isReadStatus() {
        return readStatus;
    }

    public void setReadStatus(boolean readStatus) {
        this.readStatus = readStatus;
    }

    public List<Long> getNotificationIds() {
        return notificationIds;
    }

    public void setNotificationIds(List<Long> notificationIds) {
        this.notificationIds = notificationIds;
    }
}
