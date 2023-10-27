package com.bezkoder.spring.security.postgresql.repository;

import com.bezkoder.spring.security.postgresql.models.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
    List<Challenge> findAllByOrderByIdDesc();
    List<Challenge> findAllByOrderByIdAsc();
    Optional<Challenge> findById(Long id);

    @Query(value = "SELECT c.id AS challenge_id, c.name AS challenge_name, c.description AS challenge_description, " +
            "CAST(jsonb_agg(DISTINCT jsonb_build_object('user_photo', u.photo)) AS TEXT) AS users " +
            "FROM challenges c " +
            "JOIN user_challenge uc ON c.id = uc.challenge_id " +
            "JOIN users u ON uc.user_id = u.id " +
            "GROUP BY c.id", nativeQuery = true)
    List<Object[]> getChallengesWithUsers();

}
