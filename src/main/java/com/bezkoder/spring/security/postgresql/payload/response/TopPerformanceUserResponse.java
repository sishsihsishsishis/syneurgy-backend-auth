package com.bezkoder.spring.security.postgresql.payload.response;

public class TopPerformanceUserResponse {
    private String name;
    private String photo;

    private int percent;

    private Long user_id;

    public TopPerformanceUserResponse(String name, String photo, int percent, Long user_id) {
        this.name = name;
        this.photo = photo;
        this.percent = percent;
        this.user_id = user_id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public int getPercent() {
        return percent;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }
}
