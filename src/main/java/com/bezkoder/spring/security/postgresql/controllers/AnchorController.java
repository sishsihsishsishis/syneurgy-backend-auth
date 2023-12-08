package com.bezkoder.spring.security.postgresql.controllers;

import com.bezkoder.spring.security.postgresql.models.Superpower;
import com.bezkoder.spring.security.postgresql.repository.AnchorRepository;
import com.bezkoder.spring.security.postgresql.service.AnchorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.bezkoder.spring.security.postgresql.models.Anchor;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/anchors")
@Api(tags = "Anchor")
public class AnchorController {
    @Autowired
    AnchorRepository anchorRepository;
    @Autowired
    AnchorService anchorService;

    @GetMapping("/byComponent/{componentId}")
    public ResponseEntity<?> getAnchorsByComponentId(@PathVariable Long componentId) {
        List<Anchor> anchors = anchorService.getAnchorsByComponentId(componentId);
        if (anchors.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(anchors, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ApiOperation("Get Anchor by id")
    public ResponseEntity<?> getAnchorById(@PathVariable Long id) {
        Optional<Anchor> anchor = anchorRepository.findById(id);
        return anchor.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
