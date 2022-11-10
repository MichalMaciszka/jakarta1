package org.example.team.controller;

import lombok.NoArgsConstructor;
import org.example.team.dto.CreateTeamRequest;
import org.example.team.dto.GetTeamResponse;
import org.example.team.dto.GetTeamsResponse;
import org.example.team.dto.UpdateTeamRequest;
import org.example.team.entity.Team;
import org.example.team.service.TeamService;

import javax.inject.Inject;
import javax.transaction.RollbackException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.Optional;

@NoArgsConstructor
@Path("/teams")
public class TeamController {
    private TeamService teamService;

    @Inject
    public void setTeamService(TeamService teamService) {
        this.teamService = teamService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTeams() {
        return Response
                .ok(GetTeamsResponse.entityToDtoMapper().apply(teamService.findAllTeams()))
                .build();
    }

    @GET
    @Path("{teamName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTeam(@PathParam("teamName") String teamName) {
        Optional<Team> team = teamService.findTeam(teamName);
        if (team.isPresent()) {
            return Response.ok(GetTeamResponse.entityToDtoMapper().apply(team.get())).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createTeam(CreateTeamRequest request) {
        Team team = CreateTeamRequest.dtoToEntityMapper().apply(request);
        try {
            teamService.createTeam(team);
            return Response.created(
                    UriBuilder.fromMethod(this.getClass(), "getTeam").build(team.getTeamName())
            ).build();
        } catch (RollbackException ex) {
            return Response.status(Response.Status.CONFLICT).build();
        }
    }

    @DELETE
    @Path("{teamName}")
    public Response deleteTeam(@PathParam("teamName") String teamName) {
        teamService.deleteTeam(teamName);
        return Response.noContent().build();
    }

    @DELETE
    public Response deleteAll() {
        teamService.deleteAll();
        return Response.noContent().build();
    }

    @PUT
    @Path("{teamName}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateTeam(@PathParam("teamName") String teamName, UpdateTeamRequest request) {
        Optional<Team> teamOpt = teamService.findTeam(teamName);
        if (teamOpt.isPresent()) {
            //update
            Team team = teamOpt.get();
            team.setTeamChief(request.getTeamChief());
            team.setNationality(request.getNationality());
            team.setIsActive(request.getIsActive());
            team.setChampionshipsWon(request.getChampionshipsWon());
            teamService.updateTeam(team);
            return Response.ok().build();
        } else {
            //create
            Team team = new Team(
                    teamName,
                    request.getNationality(),
                    request.getChampionshipsWon(),
                    request.getTeamChief(),
                    request.getIsActive()
            );
            try {
                teamService.createTeam(team);
                return Response.created(
                        UriBuilder.fromMethod(getClass(), "getTeam").build(teamName)
                ).build();
            } catch (RollbackException ex) {
                return Response.status(Response.Status.CONFLICT).build();
            }
        }
    }
}
