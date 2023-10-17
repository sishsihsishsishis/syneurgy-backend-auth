package com.bezkoder.spring.security.postgresql.payload.request;

public class TimeRange {
    private Long start;
    private Long end;

    // Constructors, getters, and setters

    // Constructor
    public TimeRange(Long start, Long end) {
        this.start = start;
        this.end = end;
    }

    // Getters and setters

    public Long getStart() {
        return start;
    }

    public void setStart(Long start) {
        this.start = start;
    }

    public Long getEnd() {
        return end;
    }

    public void setEnd(Long end) {
        this.end = end;
    }

    public boolean isStartNull() {
        return start == null;
    }

    public boolean isEndNull() {
        return end == null;
    }
}