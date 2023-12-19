package com.bezkoder.spring.security.postgresql.repository;

import com.bezkoder.spring.security.postgresql.models.Anchor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface AnchorRepository extends PagingAndSortingRepository<Anchor, Long> {
    List<Anchor> findAllByOrderByIdAsc();

    @Override
    Optional<Anchor> findById(Long aLong);

    List<Anchor> findByComponentId(Long componentId);
    Page<Anchor> findByComponentId(Long componentId, Pageable pageable);
}
