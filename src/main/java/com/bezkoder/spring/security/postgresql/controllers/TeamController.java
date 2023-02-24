package com.bezkoder.spring.security.postgresql.controllers;

import com.bezkoder.spring.security.postgresql.payload.request.UserInfoRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/team")
public class TeamController {
    @PutMapping("/basic")
    public ResponseEntity<?> fillBasicInfo(@Valid @RequestBody UserInfoRequest userInfoRequest, @RequestHeader(name = "Authorization") String token) {
        return ResponseEntity.ok("ok");
    }
}