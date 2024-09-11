package com.bezkoder.spring.security.postgresql.models;

import javax.persistence.*;

@Entity
@Table(name = "user_minutes")
public class UserMinutes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "all_minutes", nullable = false)
    private Integer allMinutes;

    @Column(name = "consumed_minutes", nullable = false)
    private Integer consumedMinutes;

    @Column(name = "added_minutes")
    private Integer addedMinutes = 0;

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

    public Integer getAllMinutes() {
        return allMinutes;
    }

    public void setAllMinutes(Integer allMinutes) {
        this.allMinutes = allMinutes;
    }

    public Integer getConsumedMinutes() {
        return consumedMinutes;
    }

    public void setConsumedMinutes(Integer consumedMinutes) {
        this.consumedMinutes = consumedMinutes;
    }

    public Integer getAddedMinutes() {
        return addedMinutes;
    }

    public void setAddedMinutes(Integer addedMinutes) {
        this.addedMinutes = addedMinutes;
    }
}