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
    private Date meetingTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    private Boolean isSent;

    public  Meeting(UserChallenge userChallenge, long meetingTime) {
        this.userChallenge = userChallenge;
        this.meetingTime = new Date(meetingTime);
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

    public Date getMeetingTime() {
        return meetingTime;
    }

    public void setMeetingTime(Date meetingTime) {
        this.meetingTime = meetingTime;
    }

    public Boolean getSent() {
        return isSent;
    }

    public void setSent(Boolean sent) {
        isSent = sent;
    }
}
