package com.bezkoder.spring.security.postgresql.controllers;

import com.bezkoder.spring.security.postgresql.models.*;
import com.bezkoder.spring.security.postgresql.payload.request.TeamRequest;
import com.bezkoder.spring.security.postgresql.payload.response.MessageResponse;
import com.bezkoder.spring.security.postgresql.payload.response.TeamResponse;
import com.bezkoder.spring.security.postgresql.payload.response.UserResponse;
import com.bezkoder.spring.security.postgresql.repository.TeamRepository;
import com.bezkoder.spring.security.postgresql.repository.UserRepository;
import com.bezkoder.spring.security.postgresql.repository.UserTeamRepository;
import com.bezkoder.spring.security.postgresql.security.jwt.JwtUtils;
import com.bezkoder.spring.security.postgresql.security.services.UserDetailsImpl;
import com.bezkoder.spring.security.postgresql.service.TeamService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.PageImpl;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
@Api(tags = "Team")
public class TeamController {
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TeamRepository teamRepository;
    @Autowired
    TeamService teamService;
    @Autowired
    UserTeamRepository userTeamRepository;
    @PostMapping("/team/create")
    public ResponseEntity<?> createTeam(@Valid @RequestBody TeamRequest teamRequest, @RequestHeader(name = "Authorization") String token) {

        String username = jwtUtils.getExistingUsername(token);

        Optional<User> existingUser = userRepository.findByUsername(username);

        if (!existingUser.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("The current user is unavailable!"));
        }
        User user = existingUser.get();
        Team team = new Team();
        team.setName(teamRequest.getTeamName());
        team.setCreatedDate(new Date());
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
                    .body(new MessageResponse("The current user is unavailable!"));
        }
        User user = existingUser.get();

        List<TeamResponse> teams = new ArrayList<>();
        Set<UserTeam> userTeams = user.getUserTeams();
        for (UserTeam userTeam: userTeams
        ) {
            Team team = userTeam.getTeam();
            TeamResponse teamResponse = this.getTeamResponse(team, search, userTeam.isActive());
            if (teamResponse != null) {
                teams.add(teamResponse);
            }
        }

        return new ResponseEntity<>(teams, HttpStatus.OK);
    }

    @GetMapping("/teams/by_page")
    public ResponseEntity<Page<TeamResponse>> searchTeams(
            @RequestParam(required = false) String search,
            Pageable pageable
    ) {
        Page<Team> teams = teamService.getTeams(search, pageable);
        List<TeamResponse> teamResponses = teamService.getTeamResponses(teams);
        Page<TeamResponse> teamResponsesPage = new PageImpl<>(teamResponses, pageable, teams.getTotalElements());

        return new ResponseEntity<>(teamResponsesPage, HttpStatus.OK);
    }

    @GetMapping("/teams/by-ids/{teamIds}")
    public ResponseEntity<?> getTeamsByIds(@PathVariable List<Long> teamIds) {
        List<Team> teams = teamService.getTeamsByIds(teamIds);
        List<TeamResponse> teamResponses = teamService.getTeamResponsesForList(teams);
        return new ResponseEntity<>(teamResponses, HttpStatus.OK);
    }

    private TeamResponse getTeamResponse(Team team, String search, boolean isActive) {
        TeamResponse teamResponse = null;
        Boolean getDeleted = team.getDeleted();
        boolean isDeleted = false;
        if (getDeleted == null) {
        } else {
            if (getDeleted) {
                isDeleted = true;
            }
        }
        if (isDeleted) {
            return null;
        }
        if (search != null && !search.isEmpty()) {
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
                            isActive,
                            userDetails.isActive(),
                            userDetails.isEmailVerified(),
                            userDetails.getCreatedDate()
                    );

                    userResponses.add(userResponse);
                }
                teamResponse = new TeamResponse(team.getId(), team.getName(), -1, userResponses);

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
                        isActive,
                        userDetails.isActive(),
                        userDetails.isEmailVerified(),
                        userDetails.getCreatedDate()
                );
                userResponses.add(userResponse);
            }
            teamResponse = new TeamResponse(team.getId(), team.getName(), -1, userResponses);
        }
        return teamResponse;
    }

    @PostMapping("/users/teams")
    public ResponseEntity<?> updateTeamName(@Valid @RequestBody TeamRequest teamRequest, @RequestHeader(name = "Authorization") String token) {
        String username = jwtUtils.getExistingUsername(token);
        Optional<User> existingUser1 = userRepository.findByUsername(username);

        if (!existingUser1.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("The current user is unavailable!"));
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
                    .body(new MessageResponse("The team is not your team!"));
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
                    .body(new MessageResponse("The current user is unavailable!"));
        }
        User currentUser = existingUser1.get();
        Set<Role> roles = currentUser.getRoles();
        final boolean[] isSuperAdmin = {false};
        roles.forEach(role -> {
            if (role.getName().equals(ERole.ROLE_SUPER_ADMIN)) {
                isSuperAdmin[0] = true;
            }
        });
        if (isSuperAdmin[0]) {
            Optional<Team> team = teamRepository.findById(teamId);
            if (!team.isPresent()) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("The team is not existing!"));
            }
            Team teamUpdate = team.get();
            teamUpdate.setDeleted(true);
            teamRepository.save(teamUpdate);
        } else {
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
                        .body(new MessageResponse("The team is not your team!"));
            }

            teamUpdate.setDeleted(true);
            teamRepository.save(teamUpdate);
        }
        return ResponseEntity.ok("Team is deleted successfully.");

    }

    @DeleteMapping("/users/userteam")
    public ResponseEntity<?> deleteUserTeam(@RequestParam(required = false) Long teamId, @RequestParam(required = false) Long userId) {
        UserTeam userTeam = userTeamRepository.findByUserIdAndTeamId(userId, teamId);
        if (userTeam == null) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("The team is not your team!"));
        }


        userTeamRepository.deleteById(userTeam.getId());
        return ResponseEntity.ok("Team Member is deleted successfully.");
    }

    @GetMapping("/team/users/{teamId}")
    public ResponseEntity<?> getTeamMembers(@PathVariable Long teamId) {

        Optional<Team> existingTeam = teamRepository.findById(teamId);
        if (!existingTeam.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("The current team is unavailable!"));
        }
        Team team = existingTeam.get();
        List<UserTeam> userTeams = userTeamRepository.findByTeamId(team.getId());
        List<UserResponse> userResponses = new ArrayList<>();
        if (!userTeams.isEmpty()) {
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
                        userTeam.isActive(),
                        userDetails.isActive(),
                        userDetails.isEmailVerified(),
                        userDetails.getCreatedDate()
                );
                userResponses.add(userResponse);
            }
        }
        return  ResponseEntity.ok(userResponses);
    }
}