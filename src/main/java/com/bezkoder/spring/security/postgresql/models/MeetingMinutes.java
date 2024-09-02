package com.bezkoder.spring.security.postgresql.models;


import javax.persistence.*;

@Entity
@Table(name = "meetings_minutes")
public class MeetingMinutes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "meeting_id", nullable = false)
    private Long meetingId;

    @Column(name = "meeting_minutes", nullable = false)
    private Integer meetingMinutes;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(Long meetingId) {
        this.meetingId = meetingId;
    }

    public Integer getMeetingMinutes() {
        return meetingMinutes;
    }

    public void setMeetingMinutes(Integer meetingMinutes) {
        this.meetingMinutes = meetingMinutes;
    }
}