package org.example.datastore;


import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;

import lombok.extern.java.Log;
import org.example.driver.entity.Driver;
import org.example.team.entity.Team;
import org.example.user.entity.User;
import org.example.utils.CloningUtility;

@Log
@ApplicationScoped
public class DataStore {
    private final Set<Team> teams = new HashSet<>();
    private final Set<Driver> drivers = new HashSet<>();
    private final Set<User> users = new HashSet<>();

    public synchronized void createDriver(Driver driver) {
        if (findDriverByNumber(driver.getStartingNumber()).isPresent()) {
            throw new IllegalArgumentException(
                    String.format("Driver %s %s already exists", driver.getName(), driver.getSurname())
            );
        }

        drivers.add(CloningUtility.clone(driver));
    }

    public synchronized void createTeam(Team team) {
        if (findTeamByName(team.getTeamName()).isPresent()) {
            throw new IllegalArgumentException(
                    String.format("Team with name %s already exists", team.getTeamName())
            );
        }

        teams.add(CloningUtility.clone(team));
    }

    public synchronized void createUser(User user) {
        if (findUserByLogin(user.getLogin()).isPresent()) {
            throw new IllegalArgumentException(
                    String.format("User with login %s already exists", user.getLogin())
            );
        }

        users.add(CloningUtility.clone(user));
    }

    public synchronized void deleteDriver(Integer number) {
        Optional<Driver> fromCollection = findDriverByNumber(number);
        if (fromCollection.isEmpty()) {
            throw new IllegalArgumentException(
                    String.format("Driver with number %d does not exist", number)
            );
        }

        drivers.remove(fromCollection.get());
    }

    public synchronized List<Driver> findAllDrivers() {
        return drivers.stream().map(CloningUtility::clone).collect(Collectors.toList());
    }

    public synchronized List<Team> findAllTeams() {
        return teams.stream().map(CloningUtility::clone).collect(Collectors.toList());
    }

    public synchronized List<User> findAllUsers() {
        return users.stream().map(CloningUtility::clone).collect(Collectors.toList());
    }

    public synchronized Optional<Driver> findDriverByNameAndSurname(String name, String surname) {
        return drivers.stream()
                .filter(x -> x.getName().equals(name) && x.getSurname().equals(surname))
                .findFirst()
                .map(CloningUtility::clone);
    }

    public synchronized Optional<Driver> findDriverByNumber(Integer number) {
        return drivers.stream()
                .filter(x -> x.getStartingNumber().equals(number))
                .findFirst()
                .map(CloningUtility::clone);
    }

    public synchronized Optional<Team> findTeamByName(String name) {
        return teams.stream()
                .filter(x -> x.getTeamName().equals(name))
                .findFirst()
                .map(CloningUtility::clone);
    }

    public synchronized Optional<User> findUserByLogin(String login) {
        return users.stream()
                .filter(x -> x.getLogin().equals(login))
                .findFirst()
                .map(CloningUtility::clone);
    }

    public synchronized void updateDriver(Driver driver) {
        Optional<Driver> fromCollection = findDriverByNumber(driver.getStartingNumber());
        if (fromCollection.isEmpty()) {
            throw new IllegalArgumentException(
                    String.format("Driver %s %s does not exist", driver.getName(), driver.getSurname())
            );
        }

        drivers.remove(fromCollection.get());
        drivers.add(driver);
    }

    public synchronized void updatePortrait(String login, byte[] portrait) {
        Optional<User> fromCollection = findUserByLogin(login);
        if(fromCollection.isEmpty()) {
            throw new IllegalArgumentException(
                    String.format("User with login %s not exist", login)
            );
        }

        User user = fromCollection.get();
        users.remove(user);
        user.setPortrait(portrait);
        users.add(CloningUtility.clone(user));
    }

    public synchronized void deleteTeam(String teamName) {
        var teamOpt = this.findTeamByName(teamName);
        if(teamOpt.isPresent()) {
            var team = teamOpt.get();
            var drivers = findDriversByTeam(teamName);
            drivers.forEach(d -> deleteDriver(d.getStartingNumber()));
            teams.remove(team);
        }
    }

    public synchronized List<Driver> findDriversByTeam(String teamName) {
        return drivers.stream()
                .filter(x -> x.getTeam().getTeamName().equals(teamName))
                .collect(Collectors.toList());
    }
}
