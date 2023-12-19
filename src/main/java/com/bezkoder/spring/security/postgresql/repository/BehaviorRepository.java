package com.bezkoder.spring.security.postgresql.repository;

import com.bezkoder.spring.security.postgresql.models.Behavior;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface BehaviorRepository extends PagingAndSortingRepository<Behavior, Long> {
    List<Behavior> findAllByOrderByIdAsc();

    List<Behavior> findByComponentId(Long componentId);
    Page<Behavior> findByComponentId(Long componentId, Pageable pageable);
}
