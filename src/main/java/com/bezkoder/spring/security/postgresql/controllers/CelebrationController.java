package com.bezkoder.spring.security.postgresql.controllers;

import com.bezkoder.spring.security.postgresql.models.Celebration;
import com.bezkoder.spring.security.postgresql.models.Superpower;
import com.bezkoder.spring.security.postgresql.repository.CelebrationRepository;
import com.bezkoder.spring.security.postgresql.service.CelebrationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/celebrations")
@Api(tags = "Celebration")
public class CelebrationController {
    @Autowired
    CelebrationRepository celebrationRepository;

    @Autowired
    CelebrationService celebrationService;

    @GetMapping
    @ApiOperation("Get All Celebrations")
    public ResponseEntity<?> getCelebrations() {
        List<Celebration> celebrations = celebrationService.getCelebrations();
        if (celebrations.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(celebrations, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    @ApiOperation("Get celebration by id")
    public ResponseEntity<?> getCelebrationById(@PathVariable Long id) {
        Optional<Celebration> celebration = celebrationRepository.findById(id);
        return celebration.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/random10")
    public List<Celebration> getRandom10Celebrations() {
        return celebrationService.getRandom10Celebrations();
    }
}
