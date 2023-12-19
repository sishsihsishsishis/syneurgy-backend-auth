package com.bezkoder.spring.security.postgresql.service;

import com.bezkoder.spring.security.postgresql.models.Behavior;
import com.bezkoder.spring.security.postgresql.repository.BehaviorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BehaviorService {
    @Autowired
    private BehaviorRepository behaviorRepository;
    public List<Behavior> getBehaviorsByComponentId(Long componentId) {
        return behaviorRepository.findByComponentId(componentId);
    }
    public Page<Behavior> getBehaviorsByComponentId(Long componentId, Pageable pageable) {
        return behaviorRepository.findByComponentId(componentId, pageable);
    }
}
