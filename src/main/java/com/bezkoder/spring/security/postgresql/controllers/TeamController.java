package com.bezkoder.spring.security.postgresql.controllers;

import com.bezkoder.spring.security.postgresql.models.Team;
import com.bezkoder.spring.security.postgresql.models.User;
import com.bezkoder.spring.security.postgresql.models.UserTeam;
import com.bezkoder.spring.security.postgresql.payload.request.TeamRequest;
import com.bezkoder.spring.security.postgresql.payload.response.MessageResponse;
import com.bezkoder.spring.security.postgresql.payload.response.TeamResponse;
import com.bezkoder.spring.security.postgresql.payload.response.UserResponse;
import com.bezkoder.spring.security.postgresql.repository.TeamRepository;
import com.bezkoder.spring.security.postgresql.repository.UserRepository;
import com.bezkoder.spring.security.postgresql.repository.UserTeamRepository;
import com.bezkoder.spring.security.postgresql.security.jwt.JwtUtils;
import com.bezkoder.spring.security.postgresql.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class TeamController {
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TeamRepository teamRepository;

    @Autowired
    UserTeamRepository userTeamRepository;
    @PostMapping("/team/create")
    public ResponseEntity<?> createTeam(@Valid @RequestBody TeamRequest teamRequest, @RequestHeader(name = "Authorization") String token) {

        String username = jwtUtils.getExistingUsername(token);

        Optional<User> existingUser = userRepository.findByUsername(username);

        if (!existingUser.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Error: The current user is not unavailable!"));
        }
        User user = existingUser.get();
        Team team = new Team();
        team.setName(teamRequest.getTeamName());
        Team newTeam = teamRepository.save(team);
        UserTeam userTeam = new UserTeam(user, newTeam, true);
        newTeam.addUserTeam(userTeam);
        teamRepository.save(newTeam);

        userTeamRepository.save(userTeam);
        user.addUserTeam(userTeam);
        userRepository.save(user);


        Integer step = teamRequest.getStep();
        if(step != null && step != -1) {
            step = step + 1;
            user.setStep(step);
            userRepository.save(user);
        }
        return ResponseEntity.ok(new TeamResponse(team.getId(), team.getName(), step, null));
    }

    @GetMapping("/teams")
    public ResponseEntity<List<Team>> getAllTeams() {
        List<Team> teams = new ArrayList<Team>();

        teamRepository.findAllByOrderByIdDesc().forEach(teams::add);

        if (teams.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(teams, HttpStatus.OK);
    }

    @GetMapping("/users/teams")
    public ResponseEntity<?> getAllTeamsByUserId(@RequestParam(required = false) String search, @RequestHeader(name = "Authorization") String token) {
        String username = jwtUtils.getExistingUsername(token);
        Optional<User> existingUser = userRepository.findByUsername(username);

        if (!existingUser.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Error: The current user is not unavailable!"));
        }
        User user = existingUser.get();

        List<TeamResponse> teams = new ArrayList<>();
        Set<UserTeam> userTeams = user.getUserTeams();
        for (UserTeam userTeam: userTeams
             ) {
            Team team = userTeam.getTeam();
            Boolean getDeleted = team.getDeleted();
            Boolean isDeleted = false;
            if (getDeleted == null) {
                isDeleted = false;
            } else {
                if (getDeleted == true) {
                    isDeleted = true;
                }
            }
            if (isDeleted == true) {
                continue;
            }
            if (search != null && search.length() > 0) {
                if (team.getName().toLowerCase().contains(search.toLowerCase())) {
                    Set<UserTeam> userTeams1 = team.getUserTeams();
                    List<UserResponse> userResponses = new ArrayList<>();
                    for (UserTeam userTeam1: userTeams1) {
                        User user1 = userTeam1.getUser();
                        UserDetailsImpl userDetails = UserDetailsImpl.build(user1);
                        List<String> roles = userDetails.getAuthorities().stream()
                                .map(item -> item.getAuthority())
                                .collect(Collectors.toList());
                        UserResponse userResponse = new UserResponse("",
                                userDetails.getId(),
                                userDetails.getUsername(),
                                userDetails.getEmail(),
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
                                userTeam.isActive()
                        );

                        userResponses.add(userResponse);
                    }
                    TeamResponse teamResponse = new TeamResponse(team.getId(), team.getName(), -1, userResponses);
                    teams.add(teamResponse);
                }
            } else {
                Set<UserTeam> userTeams1 = team.getUserTeams();
                List<UserResponse> userResponses = new ArrayList<>();
                for (UserTeam userTeam1: userTeams1) {
                    User user1 = userTeam1.getUser();
                    UserDetailsImpl userDetails = UserDetailsImpl.build(user1);
                    List<String> roles = userDetails.getAuthorities().stream()
                            .map(item -> item.getAuthority())
                            .collect(Collectors.toList());
                    UserResponse userResponse = new UserResponse("",
                            userDetails.getId(),
                            userDetails.getUsername(),
                            userDetails.getEmail(),
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
                            userTeam.isActive()
                    );
                    userResponses.add(userResponse);
                }
                TeamResponse teamResponse = new TeamResponse(team.getId(), team.getName(), -1, userResponses);
                teams.add(teamResponse);
            }
        }

        return new ResponseEntity<>(teams, HttpStatus.OK);
    }

    @PostMapping("/users/teams")
    public ResponseEntity<?> updateTeamName(@Valid @RequestBody TeamRequest teamRequest, @RequestHeader(name = "Authorization") String token) {
        String username = jwtUtils.getExistingUsername(token);
        Optional<User> existingUser1 = userRepository.findByUsername(username);

        if (!existingUser1.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: The current user is not unavailable!"));
        }
        User currentUser = existingUser1.get();

        Set<UserTeam> userTeams = currentUser.getUserTeams();

        Team teamUpdate = null;
        Long teamId = teamRequest.getTeamId();
        for (UserTeam userTeam: userTeams) {
            Team team = userTeam.getTeam();
            if (team.getId() == teamId) {
                teamUpdate = team;
                break;
            }
        }

        if (teamUpdate == null) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: The team is not your team!"));
        }

        String newTeamName = teamRequest.getTeamName();
        teamUpdate.setName(newTeamName);
        teamRepository.save(teamUpdate);

        return ResponseEntity.ok("Team Name is updated successfully.");

    }

    @DeleteMapping("/users/teams")
    public ResponseEntity<?> deleteTeam(@RequestParam(required = false) Long teamId, @RequestHeader(name = "Authorization") String token) {
        String username = jwtUtils.getExistingUsername(token);
        Optional<User> existingUser1 = userRepository.findByUsername(username);

        if (!existingUser1.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: The current user is not unavailable!"));
        }
        User currentUser = existingUser1.get();

        Set<UserTeam> userTeams = currentUser.getUserTeams();

        Team teamUpdate = null;

        for (UserTeam userTeam: userTeams) {
            Team team = userTeam.getTeam();
            if (team.getId() == teamId) {
                teamUpdate = team;
                break;
            }
        }

        if (teamUpdate == null) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: The team is not your team!"));
        }

        teamUpdate.setDeleted(true);
        teamRepository.save(teamUpdate);
        return ResponseEntity.ok("Team is deleted successfully.");
    }

    @GetMapping("/team/users/{teamId}")
    public ResponseEntity<?> getTeamMembers(@PathVariable Long teamId, @RequestHeader(name = "Authorization") String token) {
        String username = jwtUtils.getExistingUsername(token);

        Optional<User> existingUser = userRepository.findByUsername(username);

        if (!existingUser.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Error: The current user is not unavailable!"));
        }
        User user = existingUser.get();


        Optional<Team> existingTeam = teamRepository.findById(teamId);
        if (!existingTeam.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Error: The current team is not unavailable!"));
        }
        Team team = existingTeam.get();
        List<UserTeam> userTeams = userTeamRepository.findByTeamId(team.getId());
        List<UserResponse> userResponses = new ArrayList<>();
        if (userTeams.size() > 0) {
            for (UserTeam userTeam: userTeams
            ) {
                User user1 = userTeam.getUser();
                UserDetailsImpl userDetails = UserDetailsImpl.build(user1);
                List<String> roles = userDetails.getAuthorities().stream()
                        .map(item -> item.getAuthority())
                        .collect(Collectors.toList());
                UserResponse userResponse = new UserResponse("",
                        userDetails.getId(),
                        userDetails.getUsername(),
                        userDetails.getEmail(),
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
                        userTeam.isActive()
                );
                userResponses.add(userResponse);
            }
        }
        return  ResponseEntity.ok(userResponses);
    }
}