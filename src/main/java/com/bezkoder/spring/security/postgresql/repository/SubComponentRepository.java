package com.bezkoder.spring.security.postgresql.repository;


import com.bezkoder.spring.security.postgresql.models.SubComponent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubComponentRepository extends JpaRepository<SubComponent, Long> {
}