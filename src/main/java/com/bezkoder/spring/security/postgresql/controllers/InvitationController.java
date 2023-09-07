package com.bezkoder.spring.security.postgresql.controllers;

import com.bezkoder.spring.security.postgresql.models.*;
import com.bezkoder.spring.security.postgresql.payload.request.InviteRequest;
import com.bezkoder.spring.security.postgresql.payload.request.UserInfoRequest;
import com.bezkoder.spring.security.postgresql.payload.response.InviteResponse;
import com.bezkoder.spring.security.postgresql.payload.response.MessageResponse;
import com.bezkoder.spring.security.postgresql.repository.*;
import com.bezkoder.spring.security.postgresql.security.jwt.JwtUtils;
import com.bezkoder.spring.security.postgresql.service.EmailService;
import com.postmarkapp.postmark.client.exception.PostmarkException;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.*;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
@Api(tags = "Invitation")
public class InvitationController {
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    UserRepository userRepository;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    UserTeamRepository userTeamRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PasswordEncoder encoder;

    @Value("${postmark.invite-tempate-id}")
    private Integer inviteTemplateID;

    @Value("${frontend_base_url}")
    private String frontendBaseUrl;

    @PostMapping("/invitation/team")
    public ResponseEntity<?> inviteMembersToTeam(@Valid @RequestBody InviteRequest inviteRequest, @RequestHeader(name = "Authorization") String token) throws PostmarkException, IOException {
        String username = jwtUtils.getExistingUsername(token);
        Optional<User> existingUser1 = userRepository.findByUsername(username);

        if (!existingUser1.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: The current user is not unavailable!"));
        }
        User currentUser = existingUser1.get();

        List<String> emails = inviteRequest.getEmails();
        Long teamId = inviteRequest.getTeamId();
        if (teamId == null) {

            List<UserTeam> userTeams = userTeamRepository.findByUserId(currentUser.getId());
            if (userTeams.size() > 0) {
                UserTeam userTeam = userTeams.get(0);
                teamId = userTeam.getId().getTeamId();
            }
        }
        Optional<Team> existingTeam = teamRepository.findById(teamId);
        if (!existingTeam.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Error: The current team is not unavailable!"));
        }
        Team team = existingTeam.get();
        for (String email : emails
        ) {
            Optional<User> existingUser = userRepository.findByEmail(email.toLowerCase());
            if (existingUser.isPresent()) {
                User user = existingUser.get();
                UserTeamId userTeamId = new UserTeamId(user.getId(), team.getId());
                Optional<UserTeam> existingUserTeam = userTeamRepository.findById(userTeamId);
                if (existingUserTeam.isPresent()) {
                    UserTeam userTeam = existingUserTeam.get();
                    if (!userTeam.isActive()) {
                        // Add Notification
                    }
                } else {
                    UserTeam userTeam = new UserTeam(user, team, false);
                    UserTeam newUserTeam = userTeamRepository.save(userTeam);

                    user.addUserTeam(newUserTeam);
                    userRepository.save(user);

                    team.addUserTeam(newUserTeam);
                    teamRepository.save(team);
                }
            } else {
                User newUser = new User(email.toLowerCase(), email.toLowerCase(), encoder.encode("123456"));
                newUser.setInvitationToken(UUID.randomUUID().toString());
                User newUser1 = userRepository.save(newUser);

                UserTeam userTeam = new UserTeam(newUser1, team, false);
                UserTeam newUserTeam = userTeamRepository.save(userTeam);

                newUser.addUserTeam(newUserTeam);
                Set<Role> userRoles = new HashSet<>();
                Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                userRoles.add(userRole);
                newUser.setStep(0);
                newUser.setRoles(userRoles);
                newUser.setCreatedDate(new Date());
                userRepository.save(newUser);

                team.addUserTeam(newUserTeam);
                teamRepository.save(team);

//                EmailDetailsDTO emailDetailsDTO = new EmailDetailsDTO();
//                emailDetailsDTO.setRecipient(email);
//                emailDetailsDTO.setSubject("Join your team");
//                emailDetailsDTO.setMsgBody(currentUser.getFullName() + " invited you to collaborate in " + team.getName() + " \n " + frontendBaseUrl + "/confirm-invitation?token=" + newUser.getInvitationToken()+ "&id=" + team.getId());
//                emailService.sendSimpleMail(emailDetailsDTO);

                HashMap<String, Object> model = new HashMap<String, Object>();
                model.put("invite_receiver_email", email);
                model.put("action_url", frontendBaseUrl + "/confirm-invitation?token=" + newUser.getInvitationToken()+ "&id=" + team.getId());

                emailService.sendTemplateEmailWithPostmark(email, inviteTemplateID, model);

            }
        }

        return new ResponseEntity<>(new InviteResponse("Successfully Invited"), HttpStatus.OK);
    }

    @PostMapping("/reinvitation/team")
    public ResponseEntity<?> reinviteMembers(@Valid @RequestBody InviteRequest inviteRequest, @RequestHeader(name = "Authorization") String token) throws PostmarkException, IOException {
        String username = jwtUtils.getExistingUsername(token);
        Optional<User> existingUser1 = userRepository.findByUsername(username);

        if (!existingUser1.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: The current user is not unavailable!"));
        }
        User currentUser = existingUser1.get();

        Long userId = inviteRequest.getUserId();
        Long teamId = inviteRequest.getTeamId();
        if (teamId == null) {

            List<UserTeam> userTeams = userTeamRepository.findByUserId(currentUser.getId());
            if (userTeams.size() > 0) {
                UserTeam userTeam = userTeams.get(0);
                teamId = userTeam.getId().getTeamId();
            }
        }
        Optional<Team> existingTeam = teamRepository.findById(teamId);
        if (!existingTeam.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: The current team is not unavailable!"));
        }
        Team team = existingTeam.get();

        Optional<User> existingUser = userRepository.findById(userId);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            UserTeamId userTeamId = new UserTeamId(user.getId(), team.getId());
            Optional<UserTeam> existingUserTeam = userTeamRepository.findById(userTeamId);
            if (existingUserTeam.isPresent()) {
                UserTeam userTeam = existingUserTeam.get();
                if (!userTeam.isActive()) {
                    // Add Notification
                }
            } else {
                UserTeam userTeam = new UserTeam(user, team, false);
                UserTeam newUserTeam = userTeamRepository.save(userTeam);

                user.addUserTeam(newUserTeam);
                userRepository.save(user);

                team.addUserTeam(newUserTeam);
                teamRepository.save(team);
            }

//            EmailDetailsDTO emailDetailsDTO = new EmailDetailsDTO();
//            emailDetailsDTO.setRecipient(user.getEmail());
//            emailDetailsDTO.setSubject("Join your team");
//            if (user.getInvitationToken().length() > 0 ) {
//                emailDetailsDTO.setMsgBody(currentUser.getFullName() + " invited you to collaborate in " + team.getName() + " \n " + frontendBaseUrl + "/confirm-invitation?token=" + user.getInvitationToken());
//            } else {
//                emailDetailsDTO.setMsgBody(currentUser.getFullName() + " invited you to collaborate in " + team.getName() + " \n " + frontendBaseUrl + "/resend-invitation?id=" + team.getId());
//            }
//
//            emailService.sendSimpleMail(emailDetailsDTO);

            HashMap<String, Object> model = new HashMap<String, Object>();
            model.put("invite_receiver_email", user.getEmail().toLowerCase());
            if (user.getInvitationToken().length() > 0) {
                model.put("action_url", frontendBaseUrl + "/confirm-invitation?token=" + user.getInvitationToken()+ "&id=" + team.getId());
            } else {
                model.put("action_url", frontendBaseUrl + "/resend-invitation?id=" + team.getId());
            }
            emailService.sendTemplateEmailWithPostmark(user.getEmail().toLowerCase(), inviteTemplateID, model);

        } else {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: The current user is not unavailable!"));

        }

        return new ResponseEntity<>(new InviteResponse("Successfully Invited"), HttpStatus.OK);
    }

}
