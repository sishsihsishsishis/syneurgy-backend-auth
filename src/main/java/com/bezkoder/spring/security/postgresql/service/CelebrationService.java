package com.bezkoder.spring.security.postgresql.service;

import com.bezkoder.spring.security.postgresql.models.Celebration;
import com.bezkoder.spring.security.postgresql.repository.CelebrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CelebrationService {
    @Autowired
    private CelebrationRepository celebrationRepository;

    public List<Celebration> getCelebrations() {
        return celebrationRepository.findAllByOrderByIdAsc();
    }

    public List<Celebration> getRandom10Celebrations() {
        // Assuming you want to get a random set of 10 celebrations
        int limit = 10;
        return celebrationRepository.findRandomCelebrations(limit);
    }

    public Page<Celebration> getPaginatedCelebrations(Pageable pageable) {
        return celebrationRepository.findAll(pageable);
    }
}
