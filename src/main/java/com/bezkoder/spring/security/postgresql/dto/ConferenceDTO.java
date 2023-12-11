package com.bezkoder.spring.security.postgresql.dto;

import com.bezkoder.spring.security.postgresql.models.Conference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Date;

public class ConferenceDTO {
    @Getter
    private Long id;

    @Getter
    private String conferenceId;
    private String conferenceTitle;
    private Boolean isCustom;
    private Long totalConcurrentEvents;
    @Getter
    private Date conferenceStartTime;
    private Date conferenceEndTime;

    @Getter
    private Date createdDate;
    private Boolean isSent;

    @Getter
    private Long user_component_id;

    @Getter
    private Long component_id;
    @Getter
    private String component_title;

    @JsonProperty("conferenceStartTime")
    public Long getConferenceStartTimeTimestamp() {
        return conferenceStartTime != null ? conferenceStartTime.getTime() : null;
    }

    @JsonProperty("conferenceEndTime")
    public Long getConferenceEndTimeTimestamp() {
        return conferenceEndTime != null ? conferenceEndTime.getTime() : null;
    }

    @JsonProperty("createdDate")
    public Long getCreatedDateTimestamp() {
        return createdDate != null ? createdDate.getTime() : null;
    }

    public ConferenceDTO(Conference conference) {
        this.id = conference.getId();
        this.user_component_id = conference.getUserComponent().getId();
        this.component_id = conference.getUserComponent().getComponent().getId();
        this.component_title = conference.getUserComponent().getComponent().getName();
        this.conferenceId = conference.getConferenceId();
        this.conferenceTitle = conference.getConferenceTitle();
        this.isCustom = conference.getCustom();
        this.totalConcurrentEvents = conference.getTotalConcurrentEvents();
        this.conferenceStartTime = conference.getConferenceStartTime();
        this.conferenceEndTime = conference.getConferenceEndTime();
        this.createdDate = conference.getCreatedDate();
        this.isSent = conference.getSent();

    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser_component_id(Long user_component_id) {
        this.user_component_id = user_component_id;
    }

    public void setComponent_id(Long component_id) {
        this.component_id = component_id;
    }

    public void setComponent_title(String component_title) {
        this.component_title = component_title;
    }

    public void setConferenceId(String conferenceId) {
        this.conferenceId = conferenceId;
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

    public Boolean getCustom() {
        return isCustom;
    }

    public void setCustom(Boolean custom) {
        isCustom = custom;
    }

    public String getConferenceTitle() {
        return conferenceTitle;
    }

    public void setConferenceTitle(String conferenceTitle) {
        this.conferenceTitle = conferenceTitle;
    }

    public Boolean getSent() {
        return isSent;
    }

    public void setSent(Boolean sent) {
        isSent = sent;
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
