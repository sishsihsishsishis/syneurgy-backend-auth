package com.bezkoder.spring.security.postgresql.controllers;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.bezkoder.spring.security.postgresql.models.*;
import com.bezkoder.spring.security.postgresql.payload.request.AuthRequest;
import com.bezkoder.spring.security.postgresql.payload.request.ConfirmEmailRequest;
import com.bezkoder.spring.security.postgresql.payload.request.ConfirmInviteRequest;
import com.bezkoder.spring.security.postgresql.payload.request.UserInfoRequest;
import com.bezkoder.spring.security.postgresql.payload.response.TeamInfoResponse;
import com.bezkoder.spring.security.postgresql.repository.UserMinutesRepository;
import com.bezkoder.spring.security.postgresql.repository.UserTeamRepository;
import com.bezkoder.spring.security.postgresql.service.EmailService;
import com.bezkoder.spring.security.postgresql.service.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import com.bezkoder.spring.security.postgresql.payload.response.MessageResponse;
import com.bezkoder.spring.security.postgresql.payload.response.UserResponse;
import com.bezkoder.spring.security.postgresql.repository.RoleRepository;
import com.bezkoder.spring.security.postgresql.repository.UserRepository;
import com.bezkoder.spring.security.postgresql.security.jwt.JwtUtils;
import com.bezkoder.spring.security.postgresql.security.services.UserDetailsImpl;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@Api(tags = "Auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserTeamRepository userTeamRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;
    @Autowired
    private UserMinutesRepository userMinutesRepository;

    @Value("${frontend_base_url}")
    private String frontendBaseUrl;

    @Value("${postmark.confirm-email-id}")
    private Integer confirmEmailID;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody UserInfoRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail().toLowerCase(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        if (!userDetails.isEmailVerified()) {
            return new ResponseEntity<>("You need to confirm your email first.", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (!userDetails.isActive()) {
            return new ResponseEntity<>("Your account was disabled.", HttpStatus.UNPROCESSABLE_ENTITY);
        }
            List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new UserResponse(jwt,
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

    @GetMapping("/team/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        List<UserTeam> userTeams = userTeamRepository.findByTeamId(id);
        if (userTeams.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        UserTeam userTeam = userTeams.get(0);
        User currentUser = userTeam.getUser();
        String userEmail = currentUser.getEmail();
        String username = currentUser.getFullName();
        return ResponseEntity.ok(new TeamInfoResponse(username, userEmail, userTeam.getTeam().getName()));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserInfoRequest signUpRequest) {

        boolean isExistingEmail = userRepository.existsByEmail(signUpRequest.getEmail().toLowerCase());
        if (isExistingEmail) {
            Optional<User> existingUser = userRepository.findByEmail(signUpRequest.getEmail().toLowerCase());
            if (existingUser.isPresent()) {
                User user = existingUser.get();
                Set<Role> roles = user.getRoles();
                final boolean[] isAdmin = {false};
                roles.forEach(role -> {
                    if (role.getName().equals(ERole.ROLE_ADMIN)) {
                        isAdmin[0] = true;
                    }
                });
                if (isAdmin[0]) {
                    return ResponseEntity
                            .unprocessableEntity()
                            .body(new MessageResponse("Email is already in use! Please try to use another email."));

                } else {
                    String invitationToken = user.getInvitationToken();
                    if (invitationToken != null && invitationToken.length() > 0) {
                        return ResponseEntity.badRequest().body(new MessageResponse(("You were already invited. Please check your inbox or junk.")));
                    }
                    User newUser = new User(signUpRequest.getEmail().toLowerCase(),
                            signUpRequest.getEmail().toLowerCase(),
                            encoder.encode(signUpRequest.getPassword()));
                    newUser.setTokenForEmail(UUID.randomUUID().toString());
                    Set<Role> userRoles = new HashSet<>();
                    Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Role is not found."));
                    userRoles.add(userRole);
                    newUser.setRoles(userRoles);
                    newUser.setCreatedDate(new Date());
                    userRepository.save(newUser);
                    HashMap<String, Object> model = new HashMap<String, Object>();
                    model.put("confirm_email", newUser.getEmail());
                    model.put("action_url", frontendBaseUrl + "/confirm-email?email=" + newUser.getEmail()+"&token="+ newUser.getTokenForEmail());

                    emailService.sendTemplateEmailWithPostmark(newUser.getEmail(), confirmEmailID, model);
                    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
                }
            }
        } else {
            User user = new User(signUpRequest.getEmail().toLowerCase(),
                    signUpRequest.getEmail().toLowerCase(),
                    encoder.encode(signUpRequest.getPassword()));
            user.setTokenForEmail(UUID.randomUUID().toString());
            Set<Role> roles = new HashSet<>();

            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Role is not found."));
            roles.add(adminRole);
            user.setCreatedDate(new Date());
            user.setStep(0);
            user.setRoles(roles);
            User savedUser = userRepository.save(user);
            UserMinutes userMinutes = new UserMinutes();
            userMinutes.setUserId(savedUser.getId());
            userMinutes.setAllMinutes(0);
            userMinutes.setConsumedMinutes(0);

            // Save the user minutes entry to the database
            userMinutesRepository.save(userMinutes);

            HashMap<String, Object> model = new HashMap<String, Object>();
            model.put("confirm_email", user.getEmail());
            model.put("action_url", frontendBaseUrl + "/confirm-email?email=" + user.getEmail()+"&token="+user.getTokenForEmail());

            emailService.sendTemplateEmailWithPostmark(user.getEmail(), confirmEmailID, model);

            return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
        }

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/confirm-email-test")
    public ResponseEntity<?> confirmEmailTest(){
        HashMap<String, Object> model = new HashMap<String, Object>();
        String email = "carlos.guzman@techsquads.com";
        model.put("confirm_email", email);
        model.put("action_url", frontendBaseUrl + "/confirm-email?email=" + email);

        emailService.sendTemplateEmailWithPostmark(email, confirmEmailID, model);
        return ResponseEntity.ok(new MessageResponse("Confirm Email was sent successfully"));
    }

    @PostMapping("/confirm-email")
    public ResponseEntity<?> confirmEmail(@Valid @RequestBody ConfirmEmailRequest confirmEmailRequest) {
        String token = confirmEmailRequest.getToken();
        String email = confirmEmailRequest.getEmail();
        List<User> users = userRepository.findByEmailAndTokenForEmailAndIsEmailVerified(email, token, false);
        if (users.isEmpty()) {
            return new ResponseEntity<>("Can not find the user", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        User user = users.get(0);
        user.setTokenForEmail("");
        user.setEmailVerified(true);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Your email has been successfully verified."));
    }

    @PostMapping("/confirm-invitation")
    public ResponseEntity<?> confirmInvitation(@Valid @RequestBody ConfirmInviteRequest confirmRequest) {

        String token = confirmRequest.getToken();
        Optional<User> existingUser = userRepository.findByInvitationToken(token);
        if (!existingUser.isPresent()) {
            return new ResponseEntity<>("The current user is unavailable!", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        User user = existingUser.get();
        user.setInvitationToken("");
        userRepository.save(user);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail().toLowerCase(), "123456"));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        return ResponseEntity.ok(new UserResponse(jwt,
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

    @PostMapping("/token-forgotPW")
    public ResponseEntity<?> generateTokenForgotPW(@Valid @RequestBody UserInfoRequest request) {
        String email = request.getEmail().toLowerCase();
        Optional<User> existingUser = userRepository.findByEmail(email);
        if (!existingUser.isPresent()) {
            return new ResponseEntity<>("The current user is unavailable!", HttpStatus.NOT_FOUND);
        }
        User user = existingUser.get();
        if (user.getInvitationToken() != null && user.getInvitationToken().length() > 0) {
            return new ResponseEntity<>("The current user was already invited by the other team leader. Please check your email again or ask your team leader to invite again.", HttpStatus.NOT_FOUND);
        }
        String resetPWToken = UUID.randomUUID().toString();

        user.setResetPasswordToken(resetPWToken);
        userRepository.save(user);

        emailService.sendSimpleEmail(email, "Code", "Please use this link to reset your password: " + "\n" + frontendBaseUrl + "/reset-password?token=" + resetPWToken );

        return ResponseEntity.ok(new MessageResponse("Reset link is sent successfully"));
    }

    @PostMapping("/reset-PW")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody UserInfoRequest request) {
        String resetPwType = request.getReset_pw_type();
        String resetPWToken = request.getReset_password_token();
        Optional<User> existingUser;
        if (resetPwType.equals("n")) { // it means new password
            existingUser = userRepository.findByInvitationToken(resetPWToken);
        } else { // it will be f. it means forgot password
            existingUser = userRepository.findByresetPasswordToken(resetPWToken);
        }

        if (!existingUser.isPresent()) {
            return new ResponseEntity<>("The current user is unavailable!", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        String newPassword = request.getPassword();
        User user = existingUser.get();
        user.setPassword(encoder.encode(newPassword));
        if (resetPwType.equals("n")) {// new password
            user.setInvitationToken("");
            user.setTokenForEmail("");
            user.setEmailVerified(true);
        } else { // forgot password
            user.setResetPasswordToken("");
        }

        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Password is updated successfully"));
    }



}
