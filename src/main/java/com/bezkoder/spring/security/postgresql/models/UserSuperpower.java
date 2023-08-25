package com.bezkoder.spring.security.postgresql.models;

import lombok.*;

import javax.persistence.*;
import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user_superpower")
public class UserSuperpower  {
    @EmbeddedId
    private UserSuperpowerId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("superpowerId")
    @JoinColumn(name = "superpower_id")
    private Superpower superpower;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    public UserSuperpower(User user, Superpower superpower) {
        this.id = new UserSuperpowerId(user.getId(), superpower.getId());
        this.user = user;
        this.superpower = superpower;
        this.createdDate = new Date();
    }

    public void setId(UserSuperpowerId id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Superpower getSuperpower() {
        return superpower;
    }

    public UserSuperpowerId getId() {
        return id;
    }

    public void setSuperpower(Superpower superpower) {
        this.superpower = superpower;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
