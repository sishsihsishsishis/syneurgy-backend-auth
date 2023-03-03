package com.bezkoder.spring.security.postgresql.controllers;

import com.bezkoder.spring.security.postgresql.dto.EmailDetailsDTO;
import com.bezkoder.spring.security.postgresql.models.Team;
import com.bezkoder.spring.security.postgresql.models.User;
import com.bezkoder.spring.security.postgresql.models.UserTeam;
import com.bezkoder.spring.security.postgresql.models.UserTeamId;
import com.bezkoder.spring.security.postgresql.payload.request.InviteRequest;
import com.bezkoder.spring.security.postgresql.payload.request.TeamRequest;
import com.bezkoder.spring.security.postgresql.payload.response.InviteResponse;
import com.bezkoder.spring.security.postgresql.payload.response.MessageResponse;
import com.bezkoder.spring.security.postgresql.repository.TeamRepository;
import com.bezkoder.spring.security.postgresql.repository.UserRepository;
import com.bezkoder.spring.security.postgresql.repository.UserTeamRepository;
import com.bezkoder.spring.security.postgresql.security.jwt.JwtUtils;
import com.bezkoder.spring.security.postgresql.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
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

    @PostMapping("/invitation/team")
    public ResponseEntity<?> inviteMembersToTeam(@Valid @RequestBody InviteRequest inviteRequest, @RequestHeader(name = "Authorization") String token) {
        String username = jwtUtils.getExistingUsername(token);
        Optional<User> existingUser1 = userRepository.findByUsername(username);

        if (!existingUser1.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Error: The current user is not unavailable!"));
        }
        User currentUser = existingUser1.get();

        List<String> emails = inviteRequest.getEmails();
        Optional<Team> existingTeam = teamRepository.findById(inviteRequest.getTeamId());
        if (!existingTeam.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Error: The current team is not unavailable!"));
        }
        Team team = existingTeam.get();
        for (String email: emails
             ) {
            Optional<User> existingUser = userRepository.findByEmail(email);
            if (existingUser.isPresent()) {
                User user = existingUser.get();
                UserTeamId userTeamId = new UserTeamId(user.getId(), team.getId());
                Optional<UserTeam> existingUserTeam = userTeamRepository.findById(userTeamId);
                if (existingUserTeam.isPresent()) {

                } else {
                    UserTeam userTeam = new UserTeam(user, team, false);
                    UserTeam newUserTeam = userTeamRepository.save(userTeam);

                    user.addUserTeam(newUserTeam);
                    userRepository.save(user);

                    team.addUserTeam(newUserTeam);
                    teamRepository.save(team);
                }
            } else {
                User newUser = new User(email, email, "password");
                newUser.setInvitationToken(UUID.randomUUID().toString());
                User newUser1 = userRepository.save(newUser);

                UserTeam userTeam = new UserTeam(newUser1, team, false);
                UserTeam newUserTeam = userTeamRepository.save(userTeam);

                newUser.addUserTeam(newUserTeam);
                userRepository.save(newUser);

                team.addUserTeam(newUserTeam);
                teamRepository.save(team);

                EmailDetailsDTO emailDetailsDTO = new EmailDetailsDTO();
                emailDetailsDTO.setRecipient(email);
                emailDetailsDTO.setSubject("Join your team");
                emailDetailsDTO.setMsgBody(currentUser.getFullName() + " invited you to collaborate in " + team.getName() + " \n http://127.0.0.1:5173/confirm-invitation?token=" + newUser.getInvitationToken());
//                Map<String, Object> model = new HashMap<>();
//                model.put("firstName", currentUser.getFirstName());
//                model.put("lastName", currentUser.getLastName());
//                emailDetailsDTO.setModel(model);
                emailService.sendSimpleMail(emailDetailsDTO);
//                emailService.sendEmailWithTemplate(emailDetailsDTO);

            }
        }

        Integer step = inviteRequest.getStep();
        if(step != null) {
            step = step + 1;
            currentUser.setStep(step);
            userRepository.save(currentUser);
        } else {
            step = -1;
        }

        return new ResponseEntity<>( new InviteResponse("Successfully Invited", step), HttpStatus.OK);
    }

    @PostMapping("/confirm-invitation")
    public ResponseEntity<?> confirmInvitation(@Valid @RequestBody InviteRequest inviteRequest, @RequestHeader(name = "Authorization") String token) {
        String username = jwtUtils.getExistingUsername(token);
        Optional<User> existingUser1 = userRepository.findByUsername(username);

        if (!existingUser1.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Error: The current user is not unavailable!"));
        }
        User currentUser = existingUser1.get();
        return new ResponseEntity<>("ited", HttpStatus.OK);
    }
}
