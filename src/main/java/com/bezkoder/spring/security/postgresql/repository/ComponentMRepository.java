package com.bezkoder.spring.security.postgresql.repository;

import com.bezkoder.spring.security.postgresql.models.ComponentM;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ComponentMRepository extends JpaRepository<ComponentM, Long> {
    Optional<ComponentM> findByComponentAndSubcomponentAndCvAndNlp(
            String component, String subcomponent, String cv, String nlp);
}
