package org.example.team.service;

import lombok.NoArgsConstructor;
import org.example.team.entity.Team;
import org.example.team.repository.TeamRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.RollbackException;
import javax.transaction.Transactional;
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

    @Transactional
    public void createTeam(Team team) throws RollbackException {
        teamRepository.createTeam(team);
    }

    public List<Team> findAllTeams() {
        return teamRepository.findAllTeams();
    }

    public Optional<Team> findTeam(String name) {
        return teamRepository.findTeam(name);
    }

    @Transactional
    public void deleteTeam(String name) {
        teamRepository.deleteTeam(name);
    }

    @Transactional
    public void deleteAll() {
        teamRepository.deleteAll();
    }

    @Transactional
    public void updateTeam(Team team) {
        teamRepository.updateTeam(team);
    }
}
