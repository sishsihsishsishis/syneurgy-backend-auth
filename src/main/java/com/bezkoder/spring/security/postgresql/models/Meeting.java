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


    @ManyToOne
    private  UserChallenge userChallenge;

    @Temporal(TemporalType.TIMESTAMP)
    private Date meetingStartTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date meetingEndTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    private Boolean isSent;

    public  Meeting(UserChallenge userChallenge, long meetingStartTime, long meetingEndTime) {
        this.userChallenge = userChallenge;
        this.meetingStartTime = new Date(meetingStartTime);
        this.meetingEndTime = new Date(meetingEndTime);
        this.isSent = false;
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
}
