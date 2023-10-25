package com.bezkoder.spring.security.postgresql.payload.request;

public class MeetingRequest {
    private String id;
    private String title;
    private TimeRange time;
    private Boolean isCustom;
    private Long totalConcurrentEvents;

    private int meetingReminderTime;

    public String getId() {
        return id;
    }

    public boolean isIdNull() {
        return id == null;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public TimeRange getTime() {
        return time;
    }

    public void setTime(TimeRange time) {
        this.time = time;
    }

    public void setCustom(Boolean custom) {
        isCustom = custom;
    }

    public Boolean getCustom() {
        return isCustom;
    }

    public void setTotalConcurrentEvents(Long totalConcurrentEvents) {
        this.totalConcurrentEvents = totalConcurrentEvents;
    }

    public Long getTotalConcurrentEvents() {
        return totalConcurrentEvents;
    }

    public void setMeetingReminderTime(int meetingReminderTime) {
        this.meetingReminderTime = meetingReminderTime;
    }

    public int getMeetingReminderTime() {
        return meetingReminderTime;
    }
}
