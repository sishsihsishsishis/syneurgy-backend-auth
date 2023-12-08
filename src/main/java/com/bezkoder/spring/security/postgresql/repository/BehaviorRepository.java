package com.bezkoder.spring.security.postgresql.repository;

import com.bezkoder.spring.security.postgresql.models.Behavior;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BehaviorRepository extends JpaRepository<Behavior, Long> {
    List<Behavior> findAllByOrderByIdAsc();

    List<Behavior> findByComponentId(Long componentId);
}
