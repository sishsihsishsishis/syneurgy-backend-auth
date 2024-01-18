package com.bezkoder.spring.security.postgresql.controllers;

import com.bezkoder.spring.security.postgresql.payload.request.JWTRequest;
import com.bezkoder.spring.security.postgresql.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class JwtValidationController {

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/validateToken")
    public boolean validateToken(@RequestBody JWTRequest jwtToken) {
        return jwtUtils.validateJwtToken(jwtToken.getJwtToken());
    }
}