package com.bezkoder.spring.security.postgresql.repository;


import com.bezkoder.spring.security.postgresql.models.UserChallenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserChallengeRepository extends JpaRepository<UserChallenge, Long> {
    Optional<UserChallenge> findByUserIdAndChallengeId(Long userId, Long challengeId);
    List<UserChallenge> findByUserId(Long userId);

    List<UserChallenge> findByUserIdOrderByCreatedDateDesc(@Param("userId") Long userId);

    @Query(nativeQuery = true, value =
            "WITH ranked_user_challenge AS (" +
                    "  SELECT id, user_id, challenge_id, created_date, percent," +
                    "         ROW_NUMBER() OVER (PARTITION BY user_id ORDER BY percent DESC) AS rnk" +
                    "  FROM user_challenge" +
                    ")" +
                    "SELECT id, user_id, challenge_id, created_date, percent" +
                    " FROM ranked_user_challenge" +
                    " WHERE rnk = 1" +
                    " ORDER BY percent DESC" +
                    " LIMIT 5")
    List<UserChallenge> getHighestPercentageChallengesPerUser();

    @Query("SELECT uc FROM UserChallenge uc WHERE uc.user.id = :userId AND uc.isFinished = false ORDER BY uc.id DESC")
    List<UserChallenge>  findUnfinishedUserChallengesByUserId(@Param("userId") Long userId);


}
