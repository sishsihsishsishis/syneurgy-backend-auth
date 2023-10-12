package com.bezkoder.spring.security.postgresql.payload.response;

public class UserChallengeHabitResponse {
    private Long id;
    private Long user_challenge_id;
    private Long[] habit_ids;
    private String msg;

    public UserChallengeHabitResponse(Long id, Long user_challenge_id, Long[] habit_ids, String msg) {
        this.id = id;
        this.user_challenge_id = user_challenge_id;
        this.habit_ids = habit_ids;
        this.msg = msg;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Long getUser_challenge_id() {
        return user_challenge_id;
    }

    public void setUser_challenge_id(Long user_challenge_id) {
        this.user_challenge_id = user_challenge_id;
    }

    public void setHabit_ids(Long[] habit_ids) {
        this.habit_ids = habit_ids;
    }

    public Long[] getHabit_ids() {
        return habit_ids;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
