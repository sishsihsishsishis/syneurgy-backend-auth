package com.bezkoder.spring.security.postgresql.repository;

import com.bezkoder.spring.security.postgresql.models.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends PagingAndSortingRepository<Team, Long> {
    Optional<Team> findByName(String name);
    Boolean existsByName(String name);

    List<Team> findAllByOrderByIdDesc();
    List <Team> findTeamsByUserTeamsId(Long userTeamId);

//    @Query("SELECT t FROM Team t WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :searchTxt, '%')) " +
//            "ORDER BY t.createdDate DESC")
    @Query("SELECT t FROM Team t WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :searchTxt, '%')) " +
            "AND (t.isDeleted IS NULL OR t.isDeleted = false) " +
            "ORDER BY t.createdDate DESC")
    Page<Team> findTeamsByNameContaining(@Param("searchTxt") String searchTxt, Pageable pageable);
}
