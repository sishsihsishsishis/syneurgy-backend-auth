package com.bezkoder.spring.security.postgresql.service;

import com.bezkoder.spring.security.postgresql.models.Context;
import com.bezkoder.spring.security.postgresql.repository.ContextRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContextService {
    @Autowired
    private ContextRepository contextRepository;

    public Page<Context> getContexts(Pageable pageable) {
        return contextRepository.findAll(pageable);
    }
}
