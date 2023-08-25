package com.bezkoder.spring.security.postgresql.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ChallengeHabitId implements Serializable {
    @Column(name = "challenge_id")
    Long challengeId;

    @Column(name = "habit_id")
    Long habitId;

    public void setChallengeId(Long challengeId) {
        this.challengeId = challengeId;
    }

    public Long getChallengeId() {
        return challengeId;
    }

    public Long getHabitId() {
        return habitId;
    }

    public void setHabitId(Long habitId) {
        this.habitId = habitId;
    }
}
