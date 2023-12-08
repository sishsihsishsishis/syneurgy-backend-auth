package com.bezkoder.spring.security.postgresql.repository;

import com.bezkoder.spring.security.postgresql.models.Celebration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CelebrationRepository extends JpaRepository<Celebration, Long> {
    List<Celebration> findAllByOrderByIdAsc();
}
