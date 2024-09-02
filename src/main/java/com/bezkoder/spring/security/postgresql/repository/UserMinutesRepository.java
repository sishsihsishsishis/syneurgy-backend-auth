package com.bezkoder.spring.security.postgresql.repository;

import com.bezkoder.spring.security.postgresql.models.UserMinutes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserMinutesRepository extends JpaRepository<UserMinutes, Long> {
    Optional<UserMinutes> findByUserId(Long userId);
}
