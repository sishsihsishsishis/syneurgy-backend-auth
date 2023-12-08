package com.bezkoder.spring.security.postgresql.service;

import com.bezkoder.spring.security.postgresql.models.Behavior;
import com.bezkoder.spring.security.postgresql.repository.BehaviorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BehaviorService {
    @Autowired
    private BehaviorRepository behaviorRepository;
    public List<Behavior> getBehaviorsByComponentId(Long componentId) {
        return behaviorRepository.findByComponentId(componentId);
    }
}
