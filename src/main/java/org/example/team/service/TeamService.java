package org.example.team.service;

import java.util.List;
import java.util.Optional;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import lombok.NoArgsConstructor;
import org.example.driver.entity.Driver;
import org.example.driver.service.DriverService;
import org.example.team.entity.Team;
import org.example.team.repository.TeamRepository;

@ApplicationScoped
@NoArgsConstructor
public class TeamService {
    private TeamRepository teamRepository;

    private DriverService driverService;

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
}
