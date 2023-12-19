package com.bezkoder.spring.security.postgresql.service;

import com.bezkoder.spring.security.postgresql.models.Anchor;
import com.bezkoder.spring.security.postgresql.repository.AnchorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnchorService {
    @Autowired
    private AnchorRepository anchorRepository;

    public List<Anchor> getAnchorsByComponentId(Long componentId) {
        return anchorRepository.findByComponentId(componentId);
    }

    public List<Anchor> getAnchors() {
        return anchorRepository.findAllByOrderByIdAsc();
    }

    public Page<Anchor> getAnchorsByComponentId(Long componentId, Pageable pageable) {
        return anchorRepository.findByComponentId(componentId, pageable);
    }
}
