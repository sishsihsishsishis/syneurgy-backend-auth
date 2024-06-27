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
@Table(name = "conference")
public class Conference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String conferenceId;

    private String conferenceTitle;

    private Boolean isCustom;

    private Long totalConcurrentEvents;

    private int conferenceReminderTime;

    @ManyToOne
    private UserComponentNew userComponentNew;


    @Temporal(TemporalType.TIMESTAMP)

    private Date conferenceStartTime;


    @Temporal(TemporalType.TIMESTAMP)

    private Date conferenceEndTime;

    @Temporal(TemporalType.TIMESTAMP)

    private Date createdDate;


    private Boolean isSent;

    public Conference(UserComponentNew userComponentNew, long conferenceStartTime, long conferenceEndTime, String conferenceId, String conferenceTitle, Boolean isCustom, Long totalConcurrentEvents, int conferenceReminderTime) {
        this.userComponentNew = userComponentNew;
        this.conferenceStartTime = new Date(conferenceStartTime);
        this.conferenceEndTime = new Date(conferenceEndTime);
        this.isSent = false;
        this.conferenceId = conferenceId;
        this.conferenceTitle = conferenceTitle;
        this.isCustom = isCustom;
        this.totalConcurrentEvents = totalConcurrentEvents;
        this.createdDate = new Date();
        this.conferenceReminderTime = conferenceReminderTime;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setUserComponentNew(UserComponentNew userComponentNew) {
        this.userComponentNew = userComponentNew;
    }

    public UserComponentNew getUserComponentNew() {
        return userComponentNew;
    }

    public void setConferenceStartTime(Date conferenceStartTime) {
        this.conferenceStartTime = conferenceStartTime;
    }

    public Date getConferenceStartTime() {
        return conferenceStartTime;
    }

    public void setConferenceEndTime(Date conferenceEndTime) {
        this.conferenceEndTime = conferenceEndTime;
    }

    public Date getConferenceEndTime() {
        return conferenceEndTime;
    }

    public void setSent(Boolean sent) {
        isSent = sent;
    }

    public Boolean getSent() {
        return isSent;
    }

    public void setConferenceId(String conferenceId) {
        this.conferenceId = conferenceId;
    }

    public String getConferenceId() {
        return conferenceId;
    }

    public void setConferenceTitle(String conferenceTitle) {
        this.conferenceTitle = conferenceTitle;
    }

    public String getConferenceTitle() {
        return conferenceTitle;
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

    public void setConferenceReminderTime(int conferenceReminderTime) {
        this.conferenceReminderTime = conferenceReminderTime;
    }

    public int getConferenceReminderTime() {
        return conferenceReminderTime;
    }
}
