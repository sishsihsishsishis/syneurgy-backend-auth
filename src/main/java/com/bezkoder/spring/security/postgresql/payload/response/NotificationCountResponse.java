package com.bezkoder.spring.security.postgresql.payload.response;

public class NotificationCountResponse {
    private Long notificationCount;

    public NotificationCountResponse(Long notificationCount) {
        this.notificationCount = notificationCount;
    }
    public Long getNotificationCount() {
        return notificationCount;
    }

    public void setNotificationCount(Long notificationCount) {
        this.notificationCount = notificationCount;
    }
}
