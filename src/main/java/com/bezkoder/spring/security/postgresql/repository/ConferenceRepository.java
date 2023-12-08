package com.bezkoder.spring.security.postgresql.repository;

import com.bezkoder.spring.security.postgresql.models.Conference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface ConferenceRepository extends JpaRepository<Conference, Long> {
    @Query("SELECT c FROM Conference c WHERE c.conferenceStartTime > CURRENT_TIMESTAMP")
    List<Conference> findConferencesAfterNow();

    List<Conference> findByConferenceStartTimeAfterAndIsSentIsFalse(Date conferenceTime);

    List<Conference> findByUserComponentId(Long userComponentId);

    List<Conference> findByUserComponentIdAndConferenceStartTimeAfter(Long userComponentId, Date conferenceTime);

    @Query("SELECT c FROM Conference c JOIN c.userComponent uc " +
            "WHERE MONTH(c.conferenceStartTime) = :month AND YEAR(c.conferenceStartTime) = :year AND uc.user.id = :userId " +
            "ORDER BY c.conferenceStartTime")
    List<Conference> findConferencesInMonthForUser(
            @Param("month") int month,
            @Param("year") int year,
            @Param("userId") Long userId
    );

    @Query("SELECT c FROM Conference c JOIN c.userComponent uc WHERE uc.user.id = :userId")
    List<Conference> findByUserId(@Param("userId") Long userId);

    @Query("SELECT c FROM Conference c JOIN c.userComponent uc WHERE uc.user.id = :userId AND c.conferenceStartTime < CURRENT_DATE")
    List<Conference> findPastConferencesByUserId(@Param("userId") Long userId);
    List<Conference> findByIdIn(Long[] conferenceIds);
}
