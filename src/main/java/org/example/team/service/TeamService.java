package org.example.team.service;

import lombok.NoArgsConstructor;
import org.example.team.entity.Team;
import org.example.team.repository.TeamRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
@NoArgsConstructor
public class TeamService {
    private TeamRepository teamRepository;

    @Inject
    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public void createTeam(Team team) {
        teamRepository.createTeam(team);
    }

    public List<Team> findAllTeams() {
        return teamRepository.findAllTeams();
    }

    public Optional<Team> findTeam(String name) {
        return teamRepository.findTeam(name);
    }

    public void deleteTeam(String name) {
        teamRepository.deleteTeam(name);
    }

    public void deleteAll() {
        teamRepository.deleteAll();
    }

    public void updateTeam(Team team) {
        teamRepository.updateTeam(team);
    }
}
