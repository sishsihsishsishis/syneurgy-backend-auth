package com.bezkoder.spring.security.postgresql.repository;

import com.bezkoder.spring.security.postgresql.models.Anchor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AnchorRepository extends JpaRepository<Anchor, Long> {
    List<Anchor> findAllByOrderByIdAsc();

    @Override
    Optional<Anchor> findById(Long aLong);

    List<Anchor> findByComponentId(Long componentId);
}
