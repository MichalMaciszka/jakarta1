package org.example.team.service;

import lombok.NoArgsConstructor;
import org.example.team.entity.Team;
import org.example.team.repository.TeamRepository;
import org.example.user.entity.UserRole;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@Stateless
@LocalBean
@NoArgsConstructor
public class TeamService {
    private TeamRepository teamRepository;

    @Inject
    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @RolesAllowed(UserRole.ADMIN)
    public void createTeam(Team team) throws EJBTransactionRolledbackException {
        teamRepository.createTeam(team);
    }

    public List<Team> findAllTeams() {
        return teamRepository.findAllTeams();
    }

    public Optional<Team> findTeam(String name) {
        return teamRepository.findTeam(name);
    }

    @RolesAllowed(UserRole.ADMIN)
    public void deleteTeam(Team team) {
        teamRepository.deleteTeam(team);
    }

    public void deleteAll() {
        teamRepository.deleteAll();
    }

    public void updateTeam(Team team) {
        teamRepository.updateTeam(team);
    }
}
