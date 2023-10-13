package com.bezkoder.spring.security.postgresql.repository;

import com.bezkoder.spring.security.postgresql.models.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    @Query("SELECT m FROM Meeting m WHERE m.meetingStartTime > CURRENT_TIMESTAMP")
    List<Meeting> findMeetingsAfterNow();

    List<Meeting> findByMeetingStartTimeAfterAndIsSentIsFalse(Date meetingTime);
}
