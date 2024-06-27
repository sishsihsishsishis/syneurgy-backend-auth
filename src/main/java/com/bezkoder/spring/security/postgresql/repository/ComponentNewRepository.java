package com.bezkoder.spring.security.postgresql.repository;


import com.bezkoder.spring.security.postgresql.models.Component;
import com.bezkoder.spring.security.postgresql.models.ComponentNew;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ComponentNewRepository extends JpaRepository<ComponentNew, Long> {
    Optional<ComponentNew> findByName(String name);
    List<ComponentNew> findAllByOrderByIdAsc();
    Optional<ComponentNew> findById(Long id);
}