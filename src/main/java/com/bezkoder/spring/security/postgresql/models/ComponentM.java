package com.bezkoder.spring.security.postgresql.models;

import javax.persistence.*;
import java.util.List;

@Entity
public class ComponentM {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String component;
    private String subcomponent;
    private String cv;
    private String nlp;

    @ElementCollection
    private List<MBehavior> behaviors;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getSubcomponent() {
        return subcomponent;
    }

    public void setSubcomponent(String subcomponent) {
        this.subcomponent = subcomponent;
    }

    public String getCv() {
        return cv;
    }

    public void setCv(String cv) {
        this.cv = cv;
    }

    public String getNlp() {
        return nlp;
    }

    public void setNlp(String nlp) {
        this.nlp = nlp;
    }

    public List<MBehavior> getBehaviors() {
        return behaviors;
    }

    public void setBehaviors(List<MBehavior> behaviors) {
        this.behaviors = behaviors;
    }
}
