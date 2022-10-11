package org.example.driver.mapper;

import java.util.Optional;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.xml.registry.InvalidRequestException;
import lombok.NoArgsConstructor;
import org.example.driver.dto.CreateDriverForTeamRequest;
import org.example.driver.dto.CreateDriverForUserRequest;
import org.example.driver.dto.CreateDriverRequest;
import org.example.driver.entity.Driver;
import org.example.driver.service.DriverService;
import org.example.exception.NotFoundException;
import org.example.team.entity.Team;
import org.example.team.service.TeamService;
import org.example.user.entity.User;
import org.example.user.service.UserService;

@ApplicationScoped
@NoArgsConstructor
public class DriverMapper {
    private UserService userService;
    private TeamService teamService;
    private DriverService driverService;

    @Inject
    public DriverMapper(UserService userService, TeamService teamService, DriverService driverService) {
        this.userService = userService;
        this.teamService = teamService;
        this.driverService = driverService;
    }

    public Driver createDriverForUserMapping(CreateDriverForUserRequest request, String login) throws NotFoundException {
        Optional<User> user = userService.findByLogin(login);
        if(user.isEmpty()) {
            throw new NotFoundException("User not found");
        }

        Optional<Team> team = teamService.findTeam(request.getTeamName());
        if(team.isEmpty()) {
            throw new NotFoundException("Team not found");
        }

        if(driverService.findDriver(request.getStartingNumber()).isPresent()) {
            throw new IllegalStateException("Driver already exists");
        }

        return Driver.builder()
                .nationality(request.getNationality())
                .racesWon(request.getRacesWon())
                .name(request.getName())
                .surname(request.getSurname())
                .startingNumber(request.getStartingNumber())
                .team(team.get())
                .user(user.get())
                .build();
    }

    public Driver createDriverMapping(CreateDriverRequest request) throws NotFoundException {
        Optional<User> user = userService.findByLogin(request.getUserLogin());
        if(user.isEmpty()) {
            throw new NotFoundException("User not found");
        }

        Optional<Team> team = teamService.findTeam(request.getTeamName());
        if(team.isEmpty()) {
            throw new NotFoundException("Team not found");
        }

        if(driverService.findDriver(Integer.parseInt(request.getStartingNumber())).isPresent()) {
            throw new IllegalStateException("Driver already exists");
        }

        return Driver.builder()
                .startingNumber(Integer.parseInt(request.getStartingNumber()))
                .name(request.getName())
                .surname(request.getSurname())
                .nationality(request.getNationality())
                .racesWon(Integer.parseInt(request.getRacesWon()))
                .team(team.get())
                .user(user.get())
                .build();
    }

    public Driver createDriverForTeamMapper(CreateDriverForTeamRequest request, String teamName) throws NotFoundException {
        Optional<Team> team = teamService.findTeam(teamName);
        if(team.isEmpty()) {
            throw new NotFoundException("Team not found");
        }

        Optional<User> user = userService.findByLogin(request.getUserLogin());
        if(user.isEmpty()) {
            throw new NotFoundException("User not found");
        }

        if(driverService.findDriver(Integer.parseInt(request.getStartingNumber())).isPresent()) {
            throw new IllegalStateException("Driver already exists");
        }

        return Driver.builder()
                .startingNumber(Integer.parseInt(request.getStartingNumber()))
                .name(request.getName())
                .surname(request.getSurname())
                .nationality(request.getNationality())
                .racesWon(Integer.parseInt(request.getRacesWon()))
                .team(team.get())
                .user(user.get())
                .build();
    }
}
