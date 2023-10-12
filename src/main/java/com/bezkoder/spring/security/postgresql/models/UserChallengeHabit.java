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
@Table(name = "user_challenge_habit")
public class UserChallengeHabit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_challenge_id", nullable = false)
    private UserChallenge userChallenge;

    private Long[] habitIds;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    public UserChallengeHabit(UserChallenge userChallenge, Long[] habitIds) {
        this.userChallenge = userChallenge;
        this.habitIds = habitIds;
        this.createdDate = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserChallenge getUserChallenge() {
        return userChallenge;
    }

    public void setUserChallenge(UserChallenge userChallenge) {
        this.userChallenge = userChallenge;
    }

    public void setHabitIds(Long[] habitIds) {
        this.habitIds = habitIds;
    }

    public Long[] getHabitIds() {
        return habitIds;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
