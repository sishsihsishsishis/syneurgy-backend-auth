package com.bezkoder.spring.security.postgresql.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "meeting")
public class Meeting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String meetingId;

    private String meetingTitle;

    private Boolean isCustom;
    private Long totalConcurrentEvents;
    @ManyToOne
    private  UserChallenge userChallenge;

    @Temporal(TemporalType.TIMESTAMP)
    private Date meetingStartTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date meetingEndTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    private Boolean isSent;

    public  Meeting(UserChallenge userChallenge, long meetingStartTime, long meetingEndTime, String meetingId, String meetingTitle, Boolean isCustom, Long totalConcurrentEvents) {
        this.userChallenge = userChallenge;
        this.meetingStartTime = new Date(meetingStartTime);
        this.meetingEndTime = new Date(meetingEndTime);
        this.isSent = false;
        this.meetingId = meetingId;
        this.meetingTitle = meetingTitle;
        this.isCustom = isCustom;
        this.totalConcurrentEvents = totalConcurrentEvents;
        this.createdDate = new Date();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public UserChallenge getUserChallenge() {
        return userChallenge;
    }

    public void setUserChallenge(UserChallenge userChallenge) {
        this.userChallenge = userChallenge;
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

    public Boolean getSent() {
        return isSent;
    }

    public void setSent(Boolean sent) {
        isSent = sent;
    }

    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    public String getMeetingTitle() {
        return meetingTitle;
    }

    public void setMeetingTitle(String meetingTitle) {
        this.meetingTitle = meetingTitle;
    }

    public Boolean getCustom() {
        return isCustom;
    }

    public void setCustom(boolean custom) {
        isCustom = custom;
    }

    public Long getTotalConcurrentEvents() {
        return totalConcurrentEvents;
    }

    public void setTotalConcurrentEvents(Long totalConcurrentEvents) {
        this.totalConcurrentEvents = totalConcurrentEvents;
    }
}
