package org.example.driver.repository;

import lombok.NoArgsConstructor;
import org.example.driver.entity.Driver;
import org.example.team.entity.Team;
import org.example.user.entity.User;

import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Dependent
@NoArgsConstructor
public class DriverRepository {
    private EntityManager em;

    @PersistenceContext
    public void setEm(EntityManager em) {
        this.em = em;
    }

    public void createDriver(Driver driver) {
        em.persist(driver);
    }

    public void deleteDriver(Driver driver) {
        em.remove(em.find(Driver.class, driver.getStartingNumber()));
    }

    public List<Driver> findAllDrivers() {
        return em.createQuery("select d from Driver d", Driver.class).getResultList();
    }

    public Optional<Driver> findDriverByNameAndSurname(String name, String surname) {
        try {
            String query = "select d from Driver d where d.name = :name and d.surname = :surname";
            return Optional.of(
                    em.createQuery(query, Driver.class)
                            .setParameter("name", name)
                            .setParameter("surname", surname)
                            .getSingleResult()
            );
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }

    public Optional<Driver> findDriverByStartingNumber(Integer number) {
        try {
            String query = "select d from Driver d where d.startingNumber = :number";
            return Optional.of(
                    em.createQuery(query, Driver.class)
                            .setParameter("number", number)
                            .getSingleResult()
            );
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }

    public Optional<Driver> findDriverByTeamAndNumber(String teamName, Integer number) {
        try {
            String query = "select d from Driver d where d.startingNumber = :number and d.team.teamName = :teamName";
            return Optional.of(
                    em.createQuery(query, Driver.class)
                            .setParameter("number", number)
                            .setParameter("teamName", teamName)
                            .getSingleResult()
            );
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }

    public List<Driver> findDriversByTeam(String teamName) {
        String query = "select d from Driver d where d.team.teamName = :teamName";
        return em.createQuery(query, Driver.class)
                .setParameter("teamName", teamName)
                .getResultList();
    }

    public void update(Driver driver) {
        em.merge(driver);
    }

    public void detach(Driver driver) {
        em.detach(driver);
    }

    public Optional<Driver> findDriverByStartingNumberAndUser(Integer startingNumber, User user) {
        try {
            String query = "select d from Driver d where d.startingNumber = :number and d.user = :user";
            return Optional.of(
                    em.createQuery(query, Driver.class)
                            .setParameter("number", startingNumber)
                            .setParameter("user", user)
                            .getSingleResult()
            );
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }

    public List<Driver> findAllByTeamAndUser(Team team, User user) {
        String query = "select d from Driver d where d.team = :team and d.user = :user";
        return em.createQuery(query, Driver.class)
                .setParameter("team", team)
                .setParameter("user", user)
                .getResultList();
    }

    public Optional<Driver> findDriverByUserTeamAndNumber(User user, Team team, Integer number) {
        try {
            String query = "select d from Driver d where d.startingNumber = :number and d.user = :user and d.team = :team";
            return Optional.of(
                    em.createQuery(query, Driver.class)
                            .setParameter("number", number)
                            .setParameter("user", user)
                            .setParameter("team", team)
                            .getSingleResult()
            );
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }
}
