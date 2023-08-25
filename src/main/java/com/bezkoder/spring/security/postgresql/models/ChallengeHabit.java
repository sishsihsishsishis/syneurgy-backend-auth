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
@Table(name = "challenge_habit")
public class ChallengeHabit {
    @EmbeddedId
    private ChallengeHabitId id;

    @ManyToOne
    @MapsId("challengeId")
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    @ManyToOne
    @MapsId("habitId")
    @JoinColumn(name = "habit_id")
    private Habit habit;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    public ChallengeHabit(Challenge challenge, Habit habit) {
        this.id = new ChallengeHabitId(challenge.getId(), habit.getId());
        this.challenge = challenge;
        this.habit = habit;
        this.createdDate = new Date();
    }

    public Challenge getChallenge() {
        return challenge;
    }

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }

    public ChallengeHabitId getId() {
        return id;
    }

    public void setId(ChallengeHabitId id) {
        this.id = id;
    }

    public Habit getHabit() {
        return habit;
    }

    public void setHabit(Habit habit) {
        this.habit = habit;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
