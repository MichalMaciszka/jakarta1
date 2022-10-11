package org.example.driver.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import org.example.datastore.DataStore;
import org.example.driver.entity.Driver;

@Dependent
public class DriverRepository {
    private final DataStore dataStore;

    @Inject
    public DriverRepository(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public Optional<Driver> findDriverByNameAndSurname(String name, String surname) {
        return dataStore.findDriverByNameAndSurname(name, surname);
    }

    public List<Driver> findAllDrivers() {
        return dataStore.findAllDrivers();
    }

    public void createDriver(Driver driver) {
        dataStore.createDriver(driver);
    }

    public void deleteDriver(Driver driver) {
        dataStore.deleteDriver(driver.getStartingNumber());
    }

    public void update(Driver driver) {
        dataStore.updateDriver(driver);
    }

    public Optional<Driver> findDriverByStartingNumber(Integer number) {
        return dataStore.findDriverByNumber(number);
    }

    public List<Driver> findDriversByUser(String login) {
        return dataStore.findAllDrivers()
                .stream()
                .filter(x -> x.getUser().getLogin().equals(login))
                .collect(Collectors.toList());
    }

    public Optional<Driver> findDriverByUserAndNumber(String login, Integer number) {
        return dataStore.findAllDrivers()
                .stream()
                .filter(x -> x.getStartingNumber().equals(number) && x.getUser().getLogin().equals(login))
                .findFirst();
    }

    public List<Driver> findDriversByTeam(String teamName) {
        return dataStore.findAllDrivers()
                .stream()
                .filter(x -> x.getTeam().getTeamName().equals(teamName))
                .collect(Collectors.toList());
    }

    public Optional<Driver> findDriverByTeamAndNumber(String teamName, Integer number) {
        return dataStore.findAllDrivers()
                .stream()
                .filter(x -> x.getTeam().getTeamName().equals(teamName) && x.getStartingNumber().equals(number))
                .findFirst();
    }
}
