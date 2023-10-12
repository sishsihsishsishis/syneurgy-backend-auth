package com.bezkoder.spring.security.postgresql.payload.request;

public class UserChallengeHabitRequest {
    private Long uChallenge_id;
    private Long[] habit_ids;

    public Long getuChallenge_id() {
        return uChallenge_id;
    }

    public void setuChallenge_id(Long uChallenge_id) {
        this.uChallenge_id = uChallenge_id;
    }

    public Long[] getHabit_ids() {
        return habit_ids;
    }

    public void setHabit_ids(Long[] habit_ids) {
        this.habit_ids = habit_ids;
    }
}
