package com.bezkoder.spring.security.postgresql.repository;


import com.bezkoder.spring.security.postgresql.models.UserChallenge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserChallengeRepository extends JpaRepository<UserChallenge, Long> {
    Optional<UserChallenge> findByUserIdAndChallengeId(Long userId, Long challengeId);
    List<UserChallenge> findByUserId(Long userId);

}
