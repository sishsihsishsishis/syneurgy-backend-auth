package com.bezkoder.spring.security.postgresql.controllers;

import com.bezkoder.spring.security.postgresql.models.Context;
import com.bezkoder.spring.security.postgresql.repository.ContextRepository;
import com.bezkoder.spring.security.postgresql.service.ContextService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/contexts")
@Api(tags = "Context")
public class ContextController {

    @Autowired
    ContextService contextService;

    @GetMapping
    public Page<Context> getContextsAll(Pageable pageable) {
        return contextService.getContexts(pageable);
    }
}
