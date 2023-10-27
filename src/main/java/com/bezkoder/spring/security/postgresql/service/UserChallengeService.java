package com.bezkoder.spring.security.postgresql.service;

import com.bezkoder.spring.security.postgresql.models.UserChallenge;
import com.bezkoder.spring.security.postgresql.repository.UserChallengeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserChallengeService {

    @Autowired
    private UserChallengeRepository userChallengeRepository;

    public List<UserChallenge> getTopFiveUC() {
        List<UserChallenge> userChallenges = userChallengeRepository.getHighestPercentageChallengesPerUser();
        return userChallenges;
    }


}