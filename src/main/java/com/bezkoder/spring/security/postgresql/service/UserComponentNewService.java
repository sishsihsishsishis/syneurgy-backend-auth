package com.bezkoder.spring.security.postgresql.service;

import com.bezkoder.spring.security.postgresql.exception.UserComponentNewNotFoundException;
import com.bezkoder.spring.security.postgresql.models.UserComponentNew;
import com.bezkoder.spring.security.postgresql.repository.UserComponentNewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class UserComponentNewService {
    @Autowired
    private UserComponentNewRepository userComponentNewRepository;

    public UserComponentNew markComponentAsFinished(Long userComponentNewId) {
        UserComponentNew userComponentNew = userComponentNewRepository.findById(userComponentNewId)
                .orElseThrow(() -> new UserComponentNewNotFoundException("UserComponent not found"));
        userComponentNew.setFinished(true);
        return userComponentNewRepository.save(userComponentNew);
    }

    public UserComponentNew updateStep(Long userComponentNewId, int newStep) {
        UserComponentNew userComponentNew = userComponentNewRepository.findById(userComponentNewId)
                .orElseThrow(() -> new UserComponentNewNotFoundException("UserComponent not found"));
        userComponentNew.setStep(newStep);
        return userComponentNewRepository.save(userComponentNew);
    }

    public List<Object> getUserComponentsByUserId(Long userId) {
        List<Map<String, Object>> results = userComponentNewRepository.getUserComponentNewsByUserId(userId);
        List<Object> categorizedComponents = new ArrayList<>();

        for (Map<String, Object> result : results) {

            categorizedComponents.add(result);
        }

        return categorizedComponents;
    }
}
