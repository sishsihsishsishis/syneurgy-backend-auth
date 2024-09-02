package com.bezkoder.spring.security.postgresql.repository;

import com.bezkoder.spring.security.postgresql.models.MeetingMinutes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MeetingMinutesRepository extends JpaRepository<MeetingMinutes, Long> {
    boolean existsByUserIdAndMeetingId(Long userId, Long meetingId);
    @Query("SELECT SUM(m.meetingMinutes) FROM MeetingMinutes m WHERE m.userId = :userId")
    Integer sumMeetingMinutesByUserId(@Param("userId") Long userId);
    Optional<MeetingMinutes> findByUserIdAndMeetingId(Long userId, Long meetingId);
}
