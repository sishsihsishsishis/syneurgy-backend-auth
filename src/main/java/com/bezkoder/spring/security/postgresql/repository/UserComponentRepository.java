package com.bezkoder.spring.security.postgresql.repository;

    import com.bezkoder.spring.security.postgresql.models.UserComponent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
    import java.util.Map;
    import java.util.Optional;

public interface UserComponentRepository extends JpaRepository<UserComponent, Long> {
    Optional<UserComponent> findByUserIdAndComponentId(Long userId, Long componentId);
    List<UserComponent> findByUserId(Long userId);

    List<UserComponent> findByUserIdOrderByCreatedDateDesc(@Param("userId") Long userId);

    @Query(nativeQuery = true, value =
            "WITH ranked_user_component AS (" +
                    "  SELECT id, user_id, component_id, created_date, percent," +
                    "         ROW_NUMBER() OVER (PARTITION BY user_id ORDER BY percent DESC) AS rnk" +
                    "  FROM user_component" +
                    ")" +
                    "SELECT id, user_id, component_id, created_date, percent" +
                    " FROM ranked_user_component" +
                    " WHERE rnk = 1" +
                    " ORDER BY percent DESC" +
                    " LIMIT 5")
    List<UserComponent> getHighestPercentageComponentsPerUser();

    @Query("SELECT uc FROM UserComponent uc WHERE uc.user.id = :userId AND uc.isFinished = false ORDER BY uc.id DESC")
    List<UserComponent>  findUnfinishedUserComponentsByUserId(@Param("userId") Long userId);

    @Query("SELECT new map(count(c.id) as count, uc.component.id as component_id, uc.component.name as component_name, uc.user.id as user_id, cp.id as competency_id, cp.name as competency_name) " +
            "FROM UserComponent uc " +
            "JOIN uc.component c " +
            "JOIN c.competency cp " +
            "WHERE uc.user.id = :userId " +
            "GROUP BY cp.id, uc.component.id, uc.component.name, uc.user.id")
    List<Map<String, Object>> getUserComponentsByUserId(@Param("userId") Long userId);
}
