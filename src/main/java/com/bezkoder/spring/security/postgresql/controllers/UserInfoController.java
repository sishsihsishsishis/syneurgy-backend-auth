package com.bezkoder.spring.security.postgresql.controllers;

import com.bezkoder.spring.security.postgresql.models.User;
import com.bezkoder.spring.security.postgresql.payload.request.UserInfoRequest;
import com.bezkoder.spring.security.postgresql.payload.response.MessageResponse;
import com.bezkoder.spring.security.postgresql.payload.response.UserInfoResponse;
import com.bezkoder.spring.security.postgresql.repository.UserRepository;
import com.bezkoder.spring.security.postgresql.security.jwt.JwtUtils;
import com.bezkoder.spring.security.postgresql.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/userinfo")
public class UserInfoController {
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @PutMapping("/basic")
    public ResponseEntity<?> fillBasicInfo(@Valid @RequestBody UserInfoRequest userInfoRequest, @RequestHeader (name="Authorization") String token) {
        String tokenOnly = "";
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            tokenOnly = token.substring(7, token.length());
        }
        String username = jwtUtils.getUserNameFromJwtToken(tokenOnly);

        Optional <User> existingUser = userRepository.findByUsername(username);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setFirstName(userInfoRequest.getFirstName());
            user.setLastName(userInfoRequest.getLastName());
            user.setCountry(userInfoRequest.getCountry());
            user.setStep(1);
            userRepository.save(user);

            return ResponseEntity.ok(new UserInfoResponse(user.getFirstName(), user.getLastName(), user.getCountry(), user.getStep()));
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: The current user is not unavailable!"));
        }

    }

}
