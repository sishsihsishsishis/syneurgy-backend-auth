package com.bezkoder.spring.security.postgresql.repository;

import com.bezkoder.spring.security.postgresql.models.Celebration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CelebrationRepository extends PagingAndSortingRepository<Celebration, Long> {
    List<Celebration> findAllByOrderByIdAsc();

    @Query(value = "SELECT * FROM celebrations ORDER BY RANDOM() LIMIT :limit", nativeQuery = true)
    List<Celebration> findRandomCelebrations(@Param("limit") int limit);
}
