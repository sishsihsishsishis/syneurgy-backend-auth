package com.bezkoder.spring.security.postgresql.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;

import javax.persistence.Embeddable;

import javax.persistence.*;

@Getter
@Entity
public class NBehavior {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;


    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "sub_component_id")

    private SubComponent subComponent;

    public NBehavior() {}
    public NBehavior(String description) {
        this.description = description;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSubComponent(SubComponent subComponent) {
        this.subComponent = subComponent;
    }
}