package com.bezkoder.spring.security.postgresql.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user_component")
public class UserComponent {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @ManyToOne
    private User user;

    @Getter
    @ManyToOne
    private Component component;

    @Getter
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column(name = "is_finished")
    private boolean isFinished = false;

    @Getter
    private int step = 1;

    @Getter
    private Long anchorId = 0L;

    @Getter
    private Long behaviorId = 0L;

    @Getter
    private Long celebrationId = 0L;

    public UserComponent(User user, Component component) {
        this.user = user;
        this.component = component;
        this.createdDate = new Date();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public void setAnchorId(Long anchorId) {
        this.anchorId = anchorId;
    }

    public void setBehaviorId(Long behaviorId) {
        this.behaviorId = behaviorId;
    }

    public void setCelebrationId(Long celebrationId) {
        this.celebrationId = celebrationId;
    }
}
