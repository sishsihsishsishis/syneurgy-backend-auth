package com.bezkoder.spring.security.postgresql.repository;

import com.bezkoder.spring.security.postgresql.models.Component;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ComponentRepository extends JpaRepository<Component, Long> {
    List<Component> findAllByOrderByIdAsc();
    Optional<Component> findById(Long id);

    @Query(value = "SELECT c.id AS component_id, c.name AS component_name, " +
            "CAST(jsonb_agg(DISTINCT jsonb_build_object('user_photo', u.photo)) AS TEXT) AS users " +
            "FROM components c " +
            "JOIN user_component uc ON c.id = uc.component_id " +
            "JOIN users u ON uc.user_id = u.id " +
            "GROUP BY c.id", nativeQuery = true)
    List<Object[]> getComponentsWithUsers();
}
