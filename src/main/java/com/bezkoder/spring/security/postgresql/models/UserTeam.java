package com.bezkoder.spring.security.postgresql.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user_team")
public class UserTeam {
    @EmbeddedId
    private UserTeamId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("teamId")
    @JoinColumn(name = "team_id")
    private Team team;

    @Column(name = "is_active")
    private boolean isActive;

    public UserTeam(User user, Team team, boolean isActive) {
        this.id = new UserTeamId(user.getId(), team.getId());
        this.user = user;
        this.team = team;
        this.isActive = isActive;
    }


}
