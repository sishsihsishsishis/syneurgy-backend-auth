package com.bezkoder.spring.security.postgresql.dto;

import com.bezkoder.spring.security.postgresql.models.Habit;

// HabitDTO
public class HabitDTO {
    private Long id;
    private String title;
    private String description;
    private Long category;

    public HabitDTO(Habit habit) {
        this.id = habit.getId();
        this.title = habit.getTitle();
        this.description = habit.getDescription();
        this.category = habit.getCategory();
    }
    // Getters and setters...


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCategory() {
        return category;
    }

    public void setCategory(Long category) {
        this.category = category;
    }
}