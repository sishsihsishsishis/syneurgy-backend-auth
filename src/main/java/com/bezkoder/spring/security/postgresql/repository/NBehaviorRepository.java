package com.bezkoder.spring.security.postgresql.repository;

import com.bezkoder.spring.security.postgresql.models.NBehavior;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NBehaviorRepository extends JpaRepository<NBehavior, Long> {
}