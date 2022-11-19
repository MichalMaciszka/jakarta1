package org.example.configuration;

import org.example.driver.entity.Driver;
import org.example.team.entity.Team;
import org.example.user.entity.User;
import org.example.user.entity.UserRole;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Singleton
@Startup
public class DataInit {
    private EntityManager em;

    @PersistenceContext
    public void setEm(EntityManager em) {
        this.em = em;
    }

    private Pbkdf2PasswordHash hash;

    @Inject
    public DataInit(Pbkdf2PasswordHash hash) {
        this.hash = hash;
    }

    public DataInit() {
    }

    @PostConstruct
    private synchronized void init() {
        User first = User.builder()
                .birthDate(LocalDate.now())
                .login("first")
                .password(hash.generate("pass".toCharArray()))
                .roles(List.of(UserRole.ADMIN, UserRole.USER))
                .build();
        User second = User.builder()
                .birthDate(LocalDate.now())
                .login("second")
                .password(hash.generate("pass2".toCharArray()))
                .roles(List.of(UserRole.USER))
                .build();
        User third = User.builder()
                .birthDate(LocalDate.now())
                .login("third")
                .password(hash.generate("pass3".toCharArray()))
                .roles(List.of(UserRole.USER))
                .build();
        User fourth = User.builder()
                .birthDate(LocalDate.now())
                .login("fourth")
                .password(hash.generate("pass4".toCharArray()))
                .roles(List.of(UserRole.ADMIN, UserRole.USER))
                .build();

        em.persist(first);
        em.persist(second);
        em.persist(third);
        em.persist(fourth);

        Team mclaren = Team.builder()
                .championshipsWon(12)
                .nationality("UK")
                .teamChief("Zak Brown")
                .teamName("McLaren")
                .isActive(true)
                .drivers(Collections.emptyList())
                .build();
        Team ferrari = Team.builder()
                .championshipsWon(15)
                .teamName("Scuderia Ferrari")
                .teamChief("Mattia Binotto")
                .nationality("Italy")
                .isActive(true)
                .drivers(Collections.emptyList())
                .build();
        Team redbull = Team.builder()
                .nationality("UK")
                .teamChief("Christian Horner")
                .teamName("RedBull Racing")
                .drivers(Collections.emptyList())
                .championshipsWon(5)
                .isActive(false)
                .build();
        Team amg = Team.builder()
                .championshipsWon(9)
                .teamName("Mercedes AMG")
                .teamChief("Toto Wolff")
                .nationality("Germany")
                .drivers(Collections.emptyList())
                .isActive(true)
                .build();

        em.persist(mclaren);
        em.persist(ferrari);
        em.persist(redbull);
        em.persist(amg);

        Driver bottas = Driver.builder()
                .name("Valtteri")
                .surname("Bottas")
                .nationality("Finland")
                .racesWon(10)
                .startingNumber(77)
                .team(amg)
                .user(fourth)
                .build();

        Driver hamilton = Driver.builder()
                .startingNumber(44)
                .team(amg)
                .name("Lewis")
                .surname("Hamilton")
                .racesWon(103)
                .nationality("UK")
                .user(second)
                .build();

        Driver leclerc = Driver.builder()
                .nationality("Monaco")
                .racesWon(5)
                .name("Charles")
                .surname("Leclerc")
                .team(ferrari)
                .startingNumber(16)
                .user(second)
                .build();

        Driver verstappen = Driver.builder()
                .startingNumber(1)
                .team(redbull)
                .name("Max")
                .surname("Verstappen")
                .racesWon(31)
                .nationality("Belgium")
                .user(first)
                .build();

        em.persist(bottas);
        em.persist(hamilton);
        em.persist(leclerc);
        em.persist(verstappen);

        ferrari.setDrivers(List.of(leclerc));
        redbull.setDrivers(List.of(verstappen));
        amg.setDrivers(List.of(hamilton, bottas));
    }
}
