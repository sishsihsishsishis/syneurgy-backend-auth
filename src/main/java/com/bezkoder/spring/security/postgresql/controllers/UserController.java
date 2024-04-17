package com.bezkoder.spring.security.postgresql.controllers;

import com.bezkoder.spring.security.postgresql.models.ERole;
import com.bezkoder.spring.security.postgresql.models.Role;
import com.bezkoder.spring.security.postgresql.models.User;
import com.bezkoder.spring.security.postgresql.payload.response.MessageResponse;
import com.bezkoder.spring.security.postgresql.payload.response.TeamResponse;
import com.bezkoder.spring.security.postgresql.payload.response.UserResponse;
import com.bezkoder.spring.security.postgresql.repository.UserRepository;
import com.bezkoder.spring.security.postgresql.security.jwt.JwtUtils;
import com.bezkoder.spring.security.postgresql.security.services.UserDetailsImpl;
import com.bezkoder.spring.security.postgresql.service.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
@Api(tags = "User")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users")
    public ResponseEntity<Page<UserResponse>> getActiveUsers(@RequestParam(required = false) String search, Pageable pageable) {
        Page<User> users = userService.getUsers(search, pageable);
        List<UserResponse> userResponses = userService.getUserResponses(users);
        Page<UserResponse> userResponsePage = new PageImpl<>(userResponses, pageable, users.getTotalElements());
        return new ResponseEntity<>(userResponsePage, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestParam Long userId, @RequestHeader(name = "Authorization") String token) {
        String username = jwtUtils.getExistingUsername(token);

        Optional<User> existingUser = userRepository.findByUsername(username);

        if (existingUser.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("The current user is unavailable!"));
        }
        User user = existingUser.get();
        Set<Role> roles = user.getRoles();
        final boolean[] isSuperAdmin = {false};
        roles.forEach(role -> {
            if (role.getName().equals(ERole.ROLE_SUPER_ADMIN)) {
                isSuperAdmin[0] = true;
            }
        });

        if (!isSuperAdmin[0]) {
            return ResponseEntity
                    .unprocessableEntity()
                    .body(new MessageResponse("You don't have a permission to delete the user"));
        }

        Optional<User> userToDelete = userRepository.findById(userId);
        if (userToDelete.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("The user with the id is unavailable"));
        }
        User user1 = userToDelete.get();
        user1.setActive(false);
        userRepository.save(user1);

        return ResponseEntity.ok(new MessageResponse("The user with the id is deleted successfully."));
    }

    @PutMapping("/{id}/paid-status")
    public ResponseEntity<?> updatePaidStatus(@PathVariable Long id, @RequestParam int paidStatus) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        user.setPaid_status(paidStatus);
        User newUser = userRepository.save(user);
        UserDetailsImpl userDetails = UserDetailsImpl.build(newUser);
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        return ResponseEntity.ok(new UserResponse("",
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
                userDetails.getPaid_status()
        ));
    }
}
