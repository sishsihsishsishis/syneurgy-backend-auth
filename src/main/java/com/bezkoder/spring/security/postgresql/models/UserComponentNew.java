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
@Table(name = "user_component_new")
public class UserComponentNew {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @ManyToOne
    private User user;

    @Getter
    @ManyToOne
    private ComponentNew componentNew;

    @Getter
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column(name = "is_finished")
    private boolean isFinished = false;

    @Getter
    private int step = 1;
    @Getter
    private Long contextId = 0L;

    @Getter
    private Long behaviorId = 0L;

    public UserComponentNew(User user, ComponentNew componentNew) {
        this.user = user;
        this.componentNew = componentNew;
        this.createdDate = new Date();
    }
    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setComponentNew(ComponentNew componentNew) {
        this.componentNew = componentNew;
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

    public void setContextId(Long contextId) {
        this.contextId = contextId;
    }

    public void setBehaviorId(Long behaviorId) {
        this.behaviorId = behaviorId;
    }
}
