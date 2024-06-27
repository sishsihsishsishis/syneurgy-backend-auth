package com.bezkoder.spring.security.postgresql.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;

import javax.persistence.*;
import java.util.List;

@Getter
@Entity
public class SubComponent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name = "";
    private String cv = "";
    private String nlp = "";

    @ManyToOne
    @JoinColumn(name = "component_new_id")
    @JsonBackReference
    private ComponentNew componentNew;

    @OneToMany(mappedBy = "subComponent", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<NBehavior> behaviors;

    public SubComponent() {}
    public SubComponent(String name, String cv, String nlp, ComponentNew componentNew) {
        this.name = name;
        this.cv = cv;
        this.nlp = nlp;
        this.componentNew = componentNew;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCv(String cv) {
        this.cv = cv;
    }

    public void setNlp(String nlp) {
        this.nlp = nlp;
    }

    public void setBehaviors(List<NBehavior> behaviors) {
        this.behaviors = behaviors;
    }

    public void setComponentNew(ComponentNew componentNew) {
        this.componentNew = componentNew;
    }
}