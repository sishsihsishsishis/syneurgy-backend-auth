package com.bezkoder.spring.security.postgresql.models;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;

import javax.persistence.*;
import java.util.List;

@Getter
@Entity
public class ComponentNew {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @OneToMany(mappedBy = "componentNew", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<SubComponent> subComponents;

    public ComponentNew(){}
    public ComponentNew(String name, String description){
        this.name = name;
        this.description = description;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSubComponents(List<SubComponent> subComponents) {
        this.subComponents = subComponents;
    }
}