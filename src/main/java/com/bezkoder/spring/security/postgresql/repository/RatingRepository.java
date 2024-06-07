package com.bezkoder.spring.security.postgresql.repository;

import com.bezkoder.spring.security.postgresql.models.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByUserIdAndMeetingIdOrderByIdAsc(Long userId, Long meetingId);
    List<Rating> findByUserIdAndMeetingIdAndStartsAndEndsOrderByIdAsc(Long userId, Long meetingId, Double starts, Double ends);
}