package com.bezkoder.spring.security.postgresql.payload.request;

public class TimeRange {
    private Long startTime;
    private Long endTime;

    // Constructors, getters, and setters

    // Constructor
    public TimeRange(Long startTime, Long endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Getters and setters
    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }
}