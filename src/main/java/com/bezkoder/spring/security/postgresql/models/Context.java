package com.bezkoder.spring.security.postgresql.models;

import lombok.Getter;

import javax.persistence.*;
@Getter
@Entity
@Table(name = "contexts")
public class Context {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;


    public Context(){}
    public Context(String description) {
        this.description = description;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
