package com.bezkoder.spring.security.postgresql.repository;

import com.bezkoder.spring.security.postgresql.models.GCParam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GCParamRepository extends JpaRepository<GCParam, Long> {
    List<GCParam> findByUserId(Long userId);
    Optional<GCParam> findById(Long id);
}
