package com.bezkoder.spring.security.postgresql.controllers;

import com.bezkoder.spring.security.postgresql.models.Team;
import com.bezkoder.spring.security.postgresql.models.User;
import com.bezkoder.spring.security.postgresql.models.UserTeam;
import com.bezkoder.spring.security.postgresql.payload.request.TeamRequest;
import com.bezkoder.spring.security.postgresql.payload.response.MessageResponse;
import com.bezkoder.spring.security.postgresql.payload.response.TeamResponse;
import com.bezkoder.spring.security.postgresql.repository.TeamRepository;
import com.bezkoder.spring.security.postgresql.repository.UserRepository;
import com.bezkoder.spring.security.postgresql.repository.UserTeamRepository;
import com.bezkoder.spring.security.postgresql.security.jwt.JwtUtils;
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
        boolean isExistingName = teamRepository.existsByName(teamRequest.getTeamName());
        if (isExistingName) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: This name is already in use!"));
        }
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

        UserTeam userTeam = new UserTeam(user, team, true);
        team.addUserTeam(userTeam);
        teamRepository.save(team);

        userTeamRepository.save(userTeam);
        user.addUserTeam(userTeam);
        userRepository.save(user);


        Integer step = teamRequest.getStep();
        if(step != null) {
            step = step + 1;
            user.setStep(step);
            userRepository.save(user);
        } else {
            step = -1;
        }
        return ResponseEntity.ok(new TeamResponse(team.getId(), team.getName(), step));
    }

    @GetMapping("/teams")
    public ResponseEntity<List<Team>> getAllTeams() {
        List<Team> teams = new ArrayList<Team>();

        teamRepository.findAll().forEach(teams::add);

        if (teams.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(teams, HttpStatus.OK);
    }

    @GetMapping("/users/teams")
    public ResponseEntity<?> getAllTeamsByUserId(@RequestHeader(name = "Authorization") String token) {
        String username = jwtUtils.getExistingUsername(token);
        Optional<User> existingUser = userRepository.findByUsername(username);

        if (!existingUser.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Error: The current user is not unavailable!"));
        }
        User user = existingUser.get();

        List<Team> teams = new ArrayList<>();
        Set<UserTeam> userTeams = user.getUserTeams();
        for (UserTeam userTeam: userTeams
             ) {
            Team team = userTeam.getTeam();
            teams.add(team);
        }

        return new ResponseEntity<>(teams, HttpStatus.OK);
    }
}