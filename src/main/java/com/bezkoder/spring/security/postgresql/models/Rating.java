package com.bezkoder.spring.security.postgresql.models;

import javax.persistence.*;

@Entity
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long meetingId;
    private Long userId;

    private int ratingValue;
    private String keyword;

    @Column(name = "is_demo", nullable = false)
    private boolean isDemo = false;
    private Double starts = (double) 0;
    private Double ends = (double) 0;

    // Constructors, getters, and setters
    // Constructor(s)
    public Rating() {
        this.ratingValue = 0; // Default rating value
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(Long meetingId) {
        this.meetingId = meetingId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(int ratingValue) {
        this.ratingValue = ratingValue;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }

    public boolean isDemo() {
        return isDemo;
    }

    public void setDemo(boolean demo) {
        isDemo = demo;
    }

    public Double getStarts() {
        return starts;
    }

    public void setStarts(Double starts) {
        this.starts = starts;
    }

    public Double getEnds() {
        return ends;
    }

    public void setEnds(Double ends) {
        this.ends = ends;
    }
}
