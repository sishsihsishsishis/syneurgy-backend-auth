package com.bezkoder.spring.security.postgresql.repository;


import com.bezkoder.spring.security.postgresql.models.UserComponentNew;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserComponentNewRepository extends JpaRepository<UserComponentNew, Long> {
    Optional<UserComponentNew> findByUserIdAndComponentNewId(Long userId, Long componentNewId);
    List<UserComponentNew> findByUserId(Long userId);
    List<UserComponentNew> findByUserIdOrderByCreatedDateDesc(@Param("userId") Long userId);

    @Query("SELECT uc FROM UserComponentNew uc WHERE uc.user.id = :userId AND uc.isFinished = false ORDER BY uc.id DESC")
    List<UserComponentNew> findUnfinishedUserComponentNewsByUserId(@Param("userId") Long userId);

    @Query("SELECT new map(count(c.id) as count, uc.componentNew.id as component_id, uc.componentNew.name as component_name, uc.user.id as user_id) " +
            "FROM UserComponentNew uc " +
            "JOIN uc.componentNew c " +
            "WHERE uc.user.id = :userId " +
            "GROUP BY uc.componentNew.id, uc.componentNew.name, uc.user.id")
    List<Map<String, Object>> getUserComponentNewsByUserId(@Param("userId") Long userId);


}
