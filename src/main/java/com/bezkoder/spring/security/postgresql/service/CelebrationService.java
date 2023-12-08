package com.bezkoder.spring.security.postgresql.service;

import com.bezkoder.spring.security.postgresql.models.Celebration;
import com.bezkoder.spring.security.postgresql.repository.CelebrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CelebrationService {
    @Autowired
    private CelebrationRepository celebrationRepository;

    public List<Celebration> getCelebrations() {
        return celebrationRepository.findAllByOrderByIdAsc();
    }
}
