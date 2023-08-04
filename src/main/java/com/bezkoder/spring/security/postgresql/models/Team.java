package com.bezkoder.spring.security.postgresql.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

//@Entity
//@Table(name = "Teams",
//        uniqueConstraints = {
//                @UniqueConstraint(columnNames = "name")
//        })

@Entity
@Table(name = "Teams")
public class Team {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Boolean isDeleted;
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<UserTeam> userTeams = new HashSet<>();
    public Team() {

    }

    public Team(String name) {
        this.name = name;
    }

    public void setUserTeams(Set<UserTeam> userTeams) {
        this.userTeams = userTeams;
    }

    public Set<UserTeam> getUserTeams() {
        return userTeams;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public void addUserTeam(UserTeam userTeam) {
        this.userTeams.add(userTeam);
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
}
