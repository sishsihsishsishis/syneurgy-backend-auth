package com.bezkoder.spring.security.postgresql.controllers;

import com.bezkoder.spring.security.postgresql.models.User;
import com.bezkoder.spring.security.postgresql.payload.request.JWTRequest;
import com.bezkoder.spring.security.postgresql.payload.response.EmailResponse;
import com.bezkoder.spring.security.postgresql.repository.UserRepository;
import com.bezkoder.spring.security.postgresql.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/jwt")
public class JwtValidationController {

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/validateToken")
    public ResponseEntity<?> validateToken(@RequestBody JWTRequest jwtToken) {
        String status = jwtUtils.validateJwtTokenWithStatus(jwtToken.getJwtToken());
        String email = "";
        if (Objects.equals(status, "success")) {
            email = jwtUtils.getUserNameFromJwtToken(jwtToken.getJwtToken());
        }
        return ResponseEntity.ok(new EmailResponse(email, status));

    }
}