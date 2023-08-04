package com.bezkoder.spring.security.postgresql.payload.response;

import com.bezkoder.spring.security.postgresql.models.User;

import java.util.List;

public class TeamResponse {

    private Long id;

    private String name;

    private Integer step;

    private List<UserResponse> users;

    public TeamResponse(Long id, String name, Integer step, List<UserResponse> users) {
        this.id = id;
        this.name = name;
        this.step = step;
        this.users = users;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public List<UserResponse> getUsers() {
        return users;
    }

    public void setUsers(List<UserResponse> users) {
        this.users = users;
    }
}
