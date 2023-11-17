package com.bezkoder.spring.security.postgresql.repository;

import com.bezkoder.spring.security.postgresql.models.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    @Query("SELECT m FROM Meeting m WHERE m.meetingStartTime > CURRENT_TIMESTAMP")
    List<Meeting> findMeetingsAfterNow();

    List<Meeting> findByMeetingStartTimeAfterAndIsSentIsFalse(Date meetingTime);

    List<Meeting> findByUserChallengeId(Long userChallengeId);
    List<Meeting> findByUserChallengeIdAndMeetingStartTimeAfter(Long userChallengeId, Date meetingTime);

    @Query("SELECT m FROM Meeting m JOIN m.userChallenge uc " +
            "WHERE MONTH(m.meetingStartTime) = :month AND YEAR(m.meetingStartTime) = :year AND uc.user.id = :userId " +
            "ORDER BY m.meetingStartTime")
    List<Meeting> findMeetingsInMonthForUser(
            @Param("month") int month,
            @Param("year") int year,
            @Param("userId") Long userId
    );

    @Query("SELECT m FROM Meeting m JOIN m.userChallenge uc WHERE uc.user.id = :userId")
    List<Meeting> findByUserId(@Param("userId") Long userId);

    @Query("SELECT m FROM Meeting m JOIN m.userChallenge uc WHERE uc.user.id = :userId AND m.meetingStartTime < CURRENT_DATE")
    List<Meeting> findPastMeetingsByUserId(@Param("userId") Long userId);
    List<Meeting> findByIdIn(Long[] meetingIds);
}
