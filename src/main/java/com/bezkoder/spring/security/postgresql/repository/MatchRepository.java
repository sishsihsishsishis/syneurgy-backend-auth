package com.bezkoder.spring.security.postgresql.repository;

import com.bezkoder.spring.security.postgresql.models.MatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MatchRepository extends JpaRepository<MatchEntity, Long> {
    MatchEntity findByUsernameAndMeetingId(String username, Long meetingId);
    Optional<MatchEntity> findTopByUserIdOrderByUpdatedDateDesc(Long userId);
    List<MatchEntity> findByMeetingId(Long meetingId);
}
