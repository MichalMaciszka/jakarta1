package org.example.configuration;

import java.time.LocalDate;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import org.example.driver.entity.Driver;
import org.example.driver.service.DriverService;
import org.example.team.entity.Team;
import org.example.team.service.TeamService;
import org.example.user.entity.User;
import org.example.user.entity.UserRole;
import org.example.user.service.UserService;

@ApplicationScoped
public class DataInitializer {
    private final UserService userService;
    private final TeamService teamService;
    private final DriverService driverService;

    @Inject
    public DataInitializer(UserService userService, TeamService teamService, DriverService driverService) {
        this.userService = userService;
        this.teamService = teamService;
        this.driverService = driverService;
    }

    public void contextInitialized(@Observes @Initialized(ApplicationScoped.class) Object o) {
        System.out.println("esssa");
        initialize();
    }

    private synchronized void initialize() {
        User first = User.builder()
                .birthDate(LocalDate.now())
                .login("first")
                .password("pass")
                .role(UserRole.ADMIN)
                .build();
        User second = User.builder()
                .birthDate(LocalDate.now())
                .login("second")
                .password("pass2")
                .role(UserRole.USER)
                .build();
        User third = User.builder()
                .birthDate(LocalDate.now())
                .login("third")
                .password("pass3")
                .role(UserRole.USER)
                .build();
        User fourth = User.builder()
                .birthDate(LocalDate.now())
                .login("fourth")
                .password("pass4")
                .role(UserRole.ADMIN)
                .build();
        userService.create(first);
        userService.create(second);
        userService.create(third);
        userService.create(fourth);

        Team mclaren = Team.builder()
                .championshipsWon(12)
                .nationality("UK")
                .teamChief("Zak Brown")
                .teamName("McLaren")
                .build();
        Team ferrari = Team.builder()
                .championshipsWon(15)
                .teamName("Scuderia Ferrari")
                .teamChief("Mattia Binotto")
                .nationality("Italy")
                .build();
        Team redbull = Team.builder()
                .nationality("UK")
                .teamChief("Christian Horner")
                .teamName("RedBull Racing")
                .championshipsWon(5)
                .build();
        Team amg = Team.builder()
                .championshipsWon(9)
                .teamName("Mercedes AMG")
                .teamChief("Toto Wolff")
                .nationality("Germany")
                .build();
        teamService.createTeam(mclaren);
        teamService.createTeam(ferrari);
        teamService.createTeam(redbull);
        teamService.createTeam(amg);

        Driver bottas = Driver.builder()
                .name("Valtteri")
                .surname("Bottas")
                .nationality("Finland")
                .racesWon(10)
                .startingNumber(77)
                .team(amg)
                .user(first)
                .build();

        Driver hamilton = Driver.builder()
                .startingNumber(44)
                .user(second)
                .team(amg)
                .name("Lewis")
                .surname("Hamilton")
                .racesWon(103)
                .nationality("UK")
                .build();

        Driver leclerc = Driver.builder()
                .nationality("Monaco")
                .racesWon(5)
                .name("Charles")
                .surname("Leclerc")
                .team(ferrari)
                .user(first)
                .startingNumber(16)
                .build();

        Driver verstappen = Driver.builder()
                .startingNumber(1)
                .user(third)
                .team(redbull)
                .name("Max")
                .surname("Verstappen")
                .racesWon(31)
                .nationality("Belgium")
                .build();
        driverService.createDriver(bottas);
        driverService.createDriver(hamilton);
        driverService.createDriver(leclerc);
        driverService.createDriver(verstappen);
    }
}
