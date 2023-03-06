package com.bezkoder.spring.security.postgresql.controllers;

import com.bezkoder.spring.security.postgresql.models.User;
import com.bezkoder.spring.security.postgresql.payload.request.ConfirmInviteRequest;
import com.bezkoder.spring.security.postgresql.payload.request.UserInfoRequest;
import com.bezkoder.spring.security.postgresql.payload.response.MessageResponse;
import com.bezkoder.spring.security.postgresql.payload.response.UserInfoResponse;
import com.bezkoder.spring.security.postgresql.repository.UserRepository;
import com.bezkoder.spring.security.postgresql.security.jwt.JwtUtils;
import com.bezkoder.spring.security.postgresql.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    PasswordEncoder encoder;
    @PutMapping("/basic")
    public ResponseEntity<?> fillBasicInfo(@Valid @RequestBody UserInfoRequest userInfoRequest, @RequestHeader (name="Authorization") String token) {

        String username = jwtUtils.getExistingUsername(token);

        Optional <User> existingUser = userRepository.findByUsername(username);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            if (userInfoRequest.getFirstName() != null) {
                user.setFirstName(userInfoRequest.getFirstName());
            }
            if (userInfoRequest.getLastName() != null) {
                user.setLastName(userInfoRequest.getLastName());
            }
            if (userInfoRequest.getCountry() != null) {
                user.setCountry(userInfoRequest.getCountry());
            }
            if (userInfoRequest.getCompany() != null) {
                user.setCompany(userInfoRequest.getCompany());
            }
            if (userInfoRequest.getPosition() != null) {
                user.setPosition(userInfoRequest.getPosition());
            }
            user.setStep(1);
            userRepository.save(user);

            return ResponseEntity.ok(new UserInfoResponse(user.getFirstName(), user.getLastName(), user.getCountry(), user.getCompany(), user.getPosition(), user.getStep()));
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: The current user is not unavailable!"));
        }

    }

    @PutMapping("/setup-password")
    public ResponseEntity<?> confirmInvitation(@Valid @RequestBody UserInfoRequest userInfoRequest, @RequestHeader(name = "Authorization") String token) {
        String username = jwtUtils.getExistingUsername(token);
        Optional<User> existingUser1 = userRepository.findByUsername(username);

        if (!existingUser1.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: The current user is not unavailable!"));
        }
        User currentUser = existingUser1.get();

        if (userInfoRequest.getPassword() != null) {
            currentUser.setPassword(encoder.encode(userInfoRequest.getPassword()));
        }
        userRepository.save(currentUser);

        return new ResponseEntity<>("Password successfully updated", HttpStatus.OK);
    }

}
