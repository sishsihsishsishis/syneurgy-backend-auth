package com.bezkoder.spring.security.postgresql.service;

import com.bezkoder.spring.security.postgresql.models.User;
import com.bezkoder.spring.security.postgresql.payload.response.UserResponse;
import com.bezkoder.spring.security.postgresql.repository.UserRepository;
import com.bezkoder.spring.security.postgresql.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    public Page<User> getUsers(String search, Pageable pageable) {
        return userRepository.findByIsActiveTrueAndSearch(search, pageable);
    }

    public List<UserResponse> getUserResponses(Page<User> users) {
        List<UserResponse> userResponses = new ArrayList<>();
        for (User user: users.getContent()) {
            UserResponse userResponse = mapUserToUserResponse(user);
            userResponses.add(userResponse);
        }
        return userResponses;
    }

    public UserResponse mapUserToUserResponse(User user) {

        UserDetailsImpl userDetails = UserDetailsImpl.build(user);
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        UserResponse userResponse = new UserResponse("",
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail().toLowerCase(),
                userDetails.getStep(),
                roles,
                userDetails.getFirstName(),
                userDetails.getLastName(),
                userDetails.getCountryCode(),
                userDetails.getCountry(),
                userDetails.getCompany(),
                userDetails.getPosition(),
                userDetails.getPhoto(),
                userDetails.getAnswers(),
                true,
                userDetails.isActive(),
                userDetails.isEmailVerified(),
                userDetails.getCreatedDate(),
                userDetails.getPaid_status(),
                userDetails.isSeenTutorialHome(),
                userDetails.isSeenTutorialMeeting()
        );
        return userResponse;
    }
}
