package org.example.driver.controller;

import lombok.NoArgsConstructor;
import org.example.driver.dto.CreateDriverForTeamRequest;
import org.example.driver.dto.GetDriverResponse;
import org.example.driver.dto.GetDriversResponse;
import org.example.driver.dto.UpdateDriverRequest;
import org.example.driver.entity.Driver;
import org.example.driver.service.DriverService;
import org.example.team.entity.Team;
import org.example.team.service.TeamService;

import javax.inject.Inject;
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
import java.util.List;
import java.util.Optional;

@NoArgsConstructor
@Path("/teams/{teamName}/drivers")
public class DriverController {
    private DriverService driverService;
    private TeamService teamService;

    @Inject
    public void setDriverService(DriverService driverService) {
        this.driverService = driverService;
    }

    @Inject
    public void setTeamService(TeamService teamService) {
        this.teamService = teamService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDrivers(@PathParam("teamName") String teamName) {
        return Response.ok(GetDriversResponse.entityToDtoMapper().apply(driverService.findDriversByTeam(teamName))).build();
    }

    @GET
    @Path("{startingNumber}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDriver(@PathParam("teamName") String teamName, @PathParam("startingNumber") Integer startingNumber) {
        Optional<Driver> driver = driverService.findDriverByTeamAndNumber(teamName, startingNumber);
        if (driver.isPresent()) {
            return Response.ok(
                    GetDriverResponse.entityToDtoMapper().apply(driver.get())
            ).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createDriver(@PathParam("teamName") String teamName, CreateDriverForTeamRequest request) {
        if (teamService.findTeam(teamName).isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Driver driver = new Driver(
                Integer.parseInt(request.getStartingNumber()),
                request.getName(),
                request.getSurname(),
                request.getNationality(),
                Integer.parseInt(request.getRacesWon()),
                teamService.findTeam(teamName).orElseThrow()
        );
        try {
            driverService.createDriver(driver);
            return Response.created(
                    UriBuilder.fromMethod(DriverController.class, "getDriver")
                            .path("drivers")
                            .path("{driver}")
                            .build(teamName, request.getStartingNumber())
            ).build();
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
            return Response.status(Response.Status.CONFLICT).build();
        }
    }

    @PUT
    @Path("{startingNumber}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateDriver(UpdateDriverRequest request, @PathParam("teamName") String teamName, @PathParam("startingNumber") Integer number) {
        if (teamService.findTeam(teamName).isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Optional<Driver> driverOpt = driverService.findDriverByTeamAndNumber(teamName, number);

        if (driverOpt.isPresent()) {
            //update
            Driver driver = driverOpt.get();

            //check if new team exists -> if no - skip field
//            Optional<Team> newTeam = teamService.findTeam(request.getNewTeamName());
//            newTeam.ifPresent(driver::setTeam);

            driver.setName(request.getName());
            driver.setSurname(request.getSurname());
            driver.setRacesWon(Integer.parseInt(request.getRacesWon()));
            driver.setNationality(request.getNationality());

            driverService.update(driver);
            return Response.ok().build();
        } else {
            //create new driver
            //check if driver with @PathParam number exists -> if yes - conflict
            if (driverService.findDriver(number).isPresent()) {
                return Response.status(Response.Status.CONFLICT).build();
            }

            //skip field: newTeamName - use one from @PathParam

            Team team = teamService.findTeam(teamName).get();
            Driver driver = new Driver(
                    number,
                    request.getName(),
                    request.getSurname(),
                    request.getNationality(),
                    Integer.parseInt(request.getRacesWon()),
                    team
            );
            driverService.createDriver(driver);
            return Response.created(
                    UriBuilder.fromMethod(DriverController.class, "getDriver")
                            .path("drivers")
                            .path("{number}")
                            .build(teamName, number)
            ).build();
        }
    }

    @DELETE
    public Response deleteAll(@PathParam("teamName") String teamName) {
        List<Driver> drivers = driverService.findDriversByTeam(teamName);
        drivers.forEach(driverService::deleteDriver);
        return Response.noContent().build();
    }

    @DELETE
    @Path("{startingNumber}")
    public Response deleteDriver(@PathParam("teamName") String teamName, @PathParam("startingNumber") Integer number) {
        Optional<Driver> driver = driverService.findDriverByTeamAndNumber(teamName, number);
        driver.ifPresent(value -> driverService.deleteDriver(value));
        return Response.noContent().build();
    }

}
