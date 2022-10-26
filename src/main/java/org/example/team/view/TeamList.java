package org.example.team.view;

import org.example.team.dto.GetTeamResponse;
import org.example.team.model.TeamsModel;
import org.example.team.service.TeamService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;


@RequestScoped
@Named
public class TeamList implements Serializable {
    private final TeamService teamService;
    private TeamsModel teams;

    @Inject
    public TeamList(TeamService teamService) {
        this.teamService = teamService;
    }

    public TeamsModel getTeams() {
        if(teams == null) {
            teams = TeamsModel.entityToModelMapper().apply(teamService.findAllTeams());
        }
        return teams;
    }

    public String deleteAction(GetTeamResponse team) {
        teamService.deleteTeam(team.getTeamName());
        return "teams_list?faces-redirect=true";
    }
}
