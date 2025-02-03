package com.bezkoder.spring.security.postgresql.service;
import com.bezkoder.spring.security.postgresql.models.Team;
import com.bezkoder.spring.security.postgresql.models.User;
import com.bezkoder.spring.security.postgresql.models.UserTeam;
import com.bezkoder.spring.security.postgresql.payload.response.TeamResponse;
import com.bezkoder.spring.security.postgresql.payload.response.UserResponse;
import com.bezkoder.spring.security.postgresql.repository.TeamRepository;
import com.bezkoder.spring.security.postgresql.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TeamService {
    @Autowired
    private TeamRepository teamRepository;

    public Page<Team> getTeams(String searchTxt, Pageable pageable) {
        return teamRepository.findTeamsByNameContaining(searchTxt.toLowerCase(), pageable);
    }

    public List<TeamResponse> getTeamResponses(Page<Team> teams) {
        List<TeamResponse> teamResponses = new ArrayList<>();

        for (Team team : teams.getContent()) {
            TeamResponse teamResponse = mapTeamToTeamResponse(team);
            teamResponses.add(teamResponse);
        }

        return teamResponses;
    }

    public List<TeamResponse> getTeamResponsesForList(List<Team> teams) {
        List<TeamResponse> teamResponses = new ArrayList<>();

        for (Team team : teams) {
            TeamResponse teamResponse = mapTeamToTeamResponse(team);
            teamResponses.add(teamResponse);
        }

        return teamResponses;
    }

    public List<Team> getTeamsByIds(List<Long> teamIds) {
        return (List<Team>) teamRepository.findAllById(teamIds);
    }

    private TeamResponse mapTeamToTeamResponse(Team team) {
        TeamResponse teamResponse = null;

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
                    true,
                    userDetails.isActive(),
                    userDetails.isEmailVerified(),
                    userDetails.getCreatedDate(),
                    userDetails.getPaid_status(),
                    userDetails.isSeenTutorialHome(),
                    userDetails.isSeenTutorialMeeting(),
                    userDetails.getZoomAccountId(),
                    userDetails.getZoomClientId(),
                    userDetails.getZoomClientSecret()
            );
            userResponses.add(userResponse);
        }
        teamResponse = new TeamResponse(team.getId(), team.getName(), -1, userResponses);

        return teamResponse;
    }
}
