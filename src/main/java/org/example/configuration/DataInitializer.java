//package org.example.configuration;
//
//import lombok.SneakyThrows;
//import org.example.driver.entity.Driver;
//import org.example.driver.service.DriverService;
//import org.example.team.entity.Team;
//import org.example.team.service.TeamService;
//import org.example.user.entity.User;
//import org.example.user.entity.UserRole;
//import org.example.user.service.UserService;
//
//import javax.enterprise.context.ApplicationScoped;
//import javax.enterprise.context.Initialized;
//import javax.enterprise.event.Observes;
//import javax.inject.Inject;
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;
//import java.time.LocalDate;
//import java.util.List;
//
//@ApplicationScoped
//public class DataInitializer {
//    private final UserService userService;
//    private final TeamService teamService;
//    private final DriverService driverService;
//
//    private final Pbkdf2PasswordHash hash;
//
//    private EntityManager em;
//
//    @Inject
//    public DataInitializer(UserService userService, TeamService teamService, DriverService driverService, Pbkdf2PasswordHash hash) {
//        this.userService = userService;
//        this.teamService = teamService;
//        this.driverService = driverService;
//        this.hash = hash;
//    }
//
//    @PersistenceContext
//    public void setEm(EntityManager em) {
//        this.em = em;
//    }
//
//    public void contextInitialized(@Observes @Initialized(ApplicationScoped.class) Object o) {
//        System.out.println("initializing...");
//        initialize();
//    }
//
//    @SneakyThrows
//    private synchronized void initialize() {
//
//        User first = User.builder()
//                .birthDate(LocalDate.now())
//                .login("first")
//                .password(hash.generate("pass".toCharArray()))
//                .roles(List.of("USER", "ADMIN"))
//                .build();
//        User second = User.builder()
//                .birthDate(LocalDate.now())
//                .login("second")
//                .password(hash.generate("pass2".toCharArray()))
//                .roles(List.of(UserRole.USER.toString()))
//                .build();
//        User third = User.builder()
//                .birthDate(LocalDate.now())
//                .login("third")
//                .password(hash.generate("pass3".toCharArray()))
//                .roles(List.of(UserRole.USER.toString()))
//                .build();
//        User fourth = User.builder()
//                .birthDate(LocalDate.now())
//                .login("fourth")
//                .password(hash.generate("pass4".toCharArray()))
//                .roles(List.of(UserRole.USER.toString(), UserRole.ADMIN.toString()))
//                .build();
//        userService.create(first);
//        userService.create(second);
//        userService.create(third);
//        userService.create(fourth);
//
//        Team mclaren = Team.builder()
//                .championshipsWon(12)
//                .nationality("UK")
//                .teamChief("Zak Brown")
//                .teamName("McLaren")
//                .isActive(true)
//                .build();
//        Team ferrari = Team.builder()
//                .championshipsWon(15)
//                .teamName("Scuderia Ferrari")
//                .teamChief("Mattia Binotto")
//                .nationality("Italy")
//                .isActive(true)
//                .build();
//        Team redbull = Team.builder()
//                .nationality("UK")
//                .teamChief("Christian Horner")
//                .teamName("RedBull Racing")
//                .championshipsWon(5)
//                .isActive(false)
//                .build();
//        Team amg = Team.builder()
//                .championshipsWon(9)
//                .teamName("Mercedes AMG")
//                .teamChief("Toto Wolff")
//                .nationality("Germany")
//                .isActive(true)
//                .build();
//        teamService.createTeam(mclaren);
//        teamService.createTeam(ferrari);
//        teamService.createTeam(redbull);
//        teamService.createTeam(amg);
//
//        Driver bottas = Driver.builder()
//                .name("Valtteri")
//                .surname("Bottas")
//                .nationality("Finland")
//                .racesWon(10)
//                .startingNumber(77)
//                .team(amg)
//                .user(fourth)
//                .build();
//
//        Driver hamilton = Driver.builder()
//                .startingNumber(44)
//                .team(amg)
//                .name("Lewis")
//                .surname("Hamilton")
//                .racesWon(103)
//                .nationality("UK")
//                .user(second)
//                .build();
//
//        Driver leclerc = Driver.builder()
//                .nationality("Monaco")
//                .racesWon(5)
//                .name("Charles")
//                .surname("Leclerc")
//                .team(ferrari)
//                .startingNumber(16)
//                .user(second)
//                .build();
//
//        Driver verstappen = Driver.builder()
//                .startingNumber(1)
//                .team(redbull)
//                .name("Max")
//                .surname("Verstappen")
//                .racesWon(31)
//                .nationality("Belgium")
//                .user(first)
//                .build();
//        driverService.createDriver(bottas);
//        driverService.createDriver(hamilton);
//        driverService.createDriver(leclerc);
//        driverService.createDriver(verstappen);
//    }
//}
