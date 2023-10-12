package com.bezkoder.spring.security.postgresql.repository;

import com.bezkoder.spring.security.postgresql.models.UserChallengeHabit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserChallengeHabitRepository extends JpaRepository<UserChallengeHabit, Long> {
    List<UserChallengeHabit> findByUserChallengeId(Long userChallengeId);
}
