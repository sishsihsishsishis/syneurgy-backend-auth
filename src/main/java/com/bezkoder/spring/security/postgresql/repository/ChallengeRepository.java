package com.bezkoder.spring.security.postgresql.repository;

import com.bezkoder.spring.security.postgresql.models.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
    List<Challenge> findAllByOrderByIdDesc();
    List<Challenge> findAllByOrderByIdAsc();
    Optional<Challenge> findById(Long id);
}
