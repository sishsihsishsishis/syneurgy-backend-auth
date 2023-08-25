package com.bezkoder.spring.security.postgresql.repository;

import com.bezkoder.spring.security.postgresql.models.Superpower;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface SuperpowerRepository extends JpaRepository<Superpower, Long> {
    List<Superpower> findAllByOrderByIdDesc();
}
