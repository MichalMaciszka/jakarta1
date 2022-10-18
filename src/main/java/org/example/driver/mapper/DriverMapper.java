package org.example.driver.mapper;

import java.util.Optional;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import lombok.NoArgsConstructor;
import org.example.driver.dto.CreateDriverForTeamRequest;
import org.example.driver.dto.CreateDriverRequest;
import org.example.driver.entity.Driver;
import org.example.driver.service.DriverService;
import org.example.exception.NotFoundException;
import org.example.team.entity.Team;
import org.example.team.service.TeamService;

@ApplicationScoped
@NoArgsConstructor
public class DriverMapper {
    private TeamService teamService;
    private DriverService driverService;

    @Inject
    public DriverMapper(TeamService teamService, DriverService driverService) {
        this.teamService = teamService;
        this.driverService = driverService;
    }

    public Driver createDriverForTeamMapper(CreateDriverForTeamRequest request, String teamName) throws NotFoundException {
        Optional<Team> team = teamService.findTeam(teamName);
        if (team.isEmpty()) {
            throw new NotFoundException("Team not found");
        }

        if (driverService.findDriver(Integer.parseInt(request.getStartingNumber())).isPresent()) {
            throw new IllegalStateException("Driver already exists");
        }

        return Driver.builder()
                .startingNumber(Integer.parseInt(request.getStartingNumber()))
                .name(request.getName())
                .surname(request.getSurname())
                .nationality(request.getNationality())
                .racesWon(Integer.parseInt(request.getRacesWon()))
                .team(team.get())
                .build();
    }

    public Driver createDriverMapping(CreateDriverRequest request) throws NotFoundException {

        Optional<Team> team = teamService.findTeam(request.getTeamName());
        if (team.isEmpty()) {
            throw new NotFoundException("Team not found");
        }

        if (driverService.findDriver(Integer.parseInt(request.getStartingNumber())).isPresent()) {
            throw new IllegalStateException("Driver already exists");
        }

        return Driver.builder()
                .startingNumber(Integer.parseInt(request.getStartingNumber()))
                .name(request.getName())
                .surname(request.getSurname())
                .nationality(request.getNationality())
                .racesWon(Integer.parseInt(request.getRacesWon()))
                .team(team.get())
                .build();
    }
}
