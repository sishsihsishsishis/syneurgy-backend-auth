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
    private  UserComponent userComponent;

    @Temporal(TemporalType.TIMESTAMP)
    private Date conferenceStartTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date conferenceEndTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    private Boolean isSent;

    public  Conference(UserComponent userComponent, long conferenceStartTime, long conferenceEndTime, String conferenceId, String conferenceTitle, Boolean isCustom, Long totalConcurrentEvents, int conferenceReminderTime) {
        this.userComponent = userComponent;
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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public UserComponent getUserComponent() {
        return userComponent;
    }

    public void setUserComponent(UserComponent userComponent) {
        this.userComponent = userComponent;
    }

    public Date getConferenceStartTime() {
        return conferenceStartTime;
    }

    public void setConferenceStartTime(Date conferenceStartTime) {
        this.conferenceStartTime = conferenceStartTime;
    }

    public Date getConferenceEndTime() {
        return conferenceEndTime;
    }

    public void setConferenceEndTime(Date conferenceEndTime) {
        this.conferenceEndTime = conferenceEndTime;
    }



    public Boolean getSent() {
        return isSent;
    }

    public void setSent(Boolean sent) {
        isSent = sent;
    }

    public String getConferenceId() {
        return conferenceId;
    }

    public void setConferenceId(String conferenceId) {
        this.conferenceId = conferenceId;
    }

    public String getConferenceTitle() {
        return conferenceTitle;
    }

    public void setConferenceTitle(String conferenceTitle) {
        this.conferenceTitle = conferenceTitle;
    }


    public Boolean getCustom() {
        return isCustom;
    }

    public void setCustom(Boolean custom) {
        isCustom = custom;
    }

    public Long getTotalConcurrentEvents() {
        return totalConcurrentEvents;
    }

    public void setTotalConcurrentEvents(Long totalConcurrentEvents) {
        this.totalConcurrentEvents = totalConcurrentEvents;
    }

    public int getConferenceReminderTime() {
        return conferenceReminderTime;
    }

    public void setConferenceReminderTime(int conferenceReminderTime) {
        this.conferenceReminderTime = conferenceReminderTime;
    }

}
