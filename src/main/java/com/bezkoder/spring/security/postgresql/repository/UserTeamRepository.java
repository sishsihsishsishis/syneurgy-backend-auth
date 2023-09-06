package com.bezkoder.spring.security.postgresql.repository;

import com.bezkoder.spring.security.postgresql.models.UserTeam;
import com.bezkoder.spring.security.postgresql.models.UserTeamId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserTeamRepository extends JpaRepository<UserTeam, UserTeamId> {

    List<UserTeam> findByUserId(Long userId);
    List<UserTeam> findByTeamId(Long teamId);

    UserTeam findByUserIdAndTeamId(Long userId, Long teamId);
}
