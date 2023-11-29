package com.bezkoder.spring.security.postgresql.service;

import com.bezkoder.spring.security.postgresql.exception.UserChallengeNotFoundException;
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

    public List<UserChallenge> getUnfinishedUserChallengesByUserId(Long userId) {
        return userChallengeRepository.findUnfinishedUserChallengesByUserId(userId);
    }

    public UserChallenge markChallengeAsFinished(Long userChallengeId) {
        UserChallenge userChallenge = userChallengeRepository.findById(userChallengeId)
                .orElseThrow(() -> new UserChallengeNotFoundException("UserChallenge not found"));

        userChallenge.setFinished(true);
        return userChallengeRepository.save(userChallenge);
    }

    public UserChallenge updateStep(Long userChallengeId, int newStep) {
        UserChallenge userChallenge = userChallengeRepository.findById(userChallengeId)
                .orElseThrow(() -> new UserChallengeNotFoundException("UserChallenge not found"));

        userChallenge.setStep(newStep);
        return userChallengeRepository.save(userChallenge);
    }
}

