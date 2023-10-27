package com.bezkoder.spring.security.postgresql.dto;

import com.bezkoder.spring.security.postgresql.models.Meeting;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

// MeetingDTO
public class MeetingDTO {
    // Include only necessary fields from Meeting and Habit
    private Long id;

    private String meetingId;
    private String meetingTitle;
    private Boolean isCustom;
    private Long totalConcurrentEvents;
    private Date meetingStartTime;
    private Date meetingEndTime;

    private Date createdDate;
    private Boolean isSent;

    private Long user_challenge_id;

    private Long habit_id;
    private String habit_title;
    private String habit_description;
    private Long habit_category;

    @JsonProperty("meetingStartTime")
    public Long getMeetingStartTimeTimestamp() {
        return meetingStartTime != null ? meetingStartTime.getTime() : null;
    }

    @JsonProperty("meetingEndTime")
    public Long getMeetingEndTimeTimestamp() {
        return meetingEndTime != null ? meetingEndTime.getTime() : null;
    }

    @JsonProperty("createdDate")
    public Long getCreatedDateTimestamp() {
        return createdDate != null ? createdDate.getTime() : null;
    }

    public MeetingDTO(Meeting meeting, HabitDTO habit) {
        this.id = meeting.getId();
        this.user_challenge_id = meeting.getUserChallenge().getId();
        this.habit_id = habit.getId();
        this.habit_title = habit.getTitle();
        this.habit_description = habit.getDescription();
        this.habit_category = habit.getCategory();
        this.meetingId = meeting.getMeetingId();
        this.meetingTitle = meeting.getMeetingTitle();
        this.isCustom = meeting.getCustom();
        this.totalConcurrentEvents = meeting.getTotalConcurrentEvents();
        this.meetingStartTime = meeting.getMeetingStartTime();
        this.meetingEndTime = meeting.getMeetingEndTime();
        this.createdDate = meeting.getCreatedDate();
        this.isSent = meeting.getSent();

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUser_challenge_id() {
        return user_challenge_id;
    }

    public void setUser_challenge_id(Long user_challenge_id) {
        this.user_challenge_id = user_challenge_id;
    }

    public Long getHabit_id() {
        return habit_id;
    }

    public void setHabit_id(Long habit_id) {
        this.habit_id = habit_id;
    }

    public String getHabit_title() {
        return habit_title;
    }

    public void setHabit_title(String habit_title) {
        this.habit_title = habit_title;
    }

    public String getHabit_description() {
        return habit_description;
    }

    public void setHabit_description(String habit_description) {
        this.habit_description = habit_description;
    }

    public Long getHabit_category() {
        return habit_category;
    }

    public void setHabit_category(Long habit_category) {
        this.habit_category = habit_category;
    }

    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    public Date getMeetingStartTime() {
        return meetingStartTime;
    }

    public void setMeetingStartTime(Date meetingStartTime) {
        this.meetingStartTime = meetingStartTime;
    }

    public Date getMeetingEndTime() {
        return meetingEndTime;
    }

    public void setMeetingEndTime(Date meetingEndTime) {
        this.meetingEndTime = meetingEndTime;
    }

    public Boolean getCustom() {
        return isCustom;
    }

    public void setCustom(Boolean custom) {
        isCustom = custom;
    }

    public String getMeetingTitle() {
        return meetingTitle;
    }

    public void setMeetingTitle(String meetingTitle) {
        this.meetingTitle = meetingTitle;
    }

    public Boolean getSent() {
        return isSent;
    }

    public void setSent(Boolean sent) {
        isSent = sent;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Long getTotalConcurrentEvents() {
        return totalConcurrentEvents;
    }

    public void setTotalConcurrentEvents(Long totalConcurrentEvents) {
        this.totalConcurrentEvents = totalConcurrentEvents;
    }
}