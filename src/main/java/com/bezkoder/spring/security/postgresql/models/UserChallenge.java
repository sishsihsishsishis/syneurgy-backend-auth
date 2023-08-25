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
@Table(name = "user_challenge")
public class UserChallenge {
    @EmbeddedId
    private UserChallengeId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("challengeId")
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column(name = "meeting_id")
    private Long meeting_id;

    public UserChallenge(User user, Challenge challenge, Long meeting_id) {
        this.id = new UserChallengeId(user.getId(), challenge.getId());
        this.user = user;
        this.challenge = challenge;
        this.createdDate = new Date();
        this.meeting_id = meeting_id;
    }

    public void setId(UserChallengeId id) {
        this.id = id;
    }

    public UserChallengeId getId() {
        return id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public Challenge getChallenge() {
        return challenge;
    }

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }

    public void setMeeting_id(Long meeting_id) {
        this.meeting_id = meeting_id;
    }

    public Long getMeeting_id() {
        return meeting_id;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getCreatedDate() {
        return createdDate;
    }
}
