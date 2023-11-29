package com.bezkoder.spring.security.postgresql.models;

import javax.persistence.*;

@Entity
@Table(name = "components")
public class Component {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @ManyToOne
    private Competency competency;

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

    public Competency getCompetency() {
        return competency;
    }

    public void setCompetency(Competency competency) {
        this.competency = competency;
    }
}
