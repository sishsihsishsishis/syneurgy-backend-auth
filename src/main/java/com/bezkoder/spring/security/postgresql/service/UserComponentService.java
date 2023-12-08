package com.bezkoder.spring.security.postgresql.service;

import com.bezkoder.spring.security.postgresql.exception.UserComponentNotFoundException;
import com.bezkoder.spring.security.postgresql.models.UserComponent;
import com.bezkoder.spring.security.postgresql.repository.UserComponentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserComponentService {
    @Autowired
    private UserComponentRepository userComponentRepository;

    public List<UserComponent> getTopFiveUC() {
        List<UserComponent> userComponents = userComponentRepository.getHighestPercentageComponentsPerUser();
        return userComponents;
    }

    public List<UserComponent> getUnfinishedUserComponentsByUserId(Long userId) {
        return userComponentRepository.findUnfinishedUserComponentsByUserId(userId);
    }

    public UserComponent markComponentAsFinished(Long userComponentId) {
        UserComponent userComponent = userComponentRepository.findById(userComponentId)
                .orElseThrow(() -> new UserComponentNotFoundException("UserComponent not found"));
        userComponent.setFinished(true);
        return userComponentRepository.save(userComponent);
    }

    public UserComponent updateStep(Long userComponentId, int newStep) {
        UserComponent userComponent = userComponentRepository.findById(userComponentId)
                .orElseThrow(() -> new UserComponentNotFoundException("UserComponent not found"));
        userComponent.setStep(newStep);
        return userComponentRepository.save(userComponent);
    }
}
