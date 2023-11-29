package com.bezkoder.spring.security.postgresql.models;

import javax.persistence.*;

@Entity
@Table(name = "behaviors")
public class Behavior {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 500)
    private String name;

    @ManyToOne
    private Component component;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }
}
