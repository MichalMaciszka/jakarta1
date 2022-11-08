package org.example.driver.repository;

import org.example.datastore.DataStore;
import org.example.driver.entity.Driver;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Dependent
public class DriverRepository {
    private final DataStore dataStore;

    @Inject
    public DriverRepository(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public void createDriver(Driver driver) {
        dataStore.createDriver(driver);
    }

    public void deleteDriver(Driver driver) {
        dataStore.deleteDriver(driver.getStartingNumber());
    }

    public List<Driver> findAllDrivers() {
        return dataStore.findAllDrivers();
    }

    public Optional<Driver> findDriverByNameAndSurname(String name, String surname) {
        return dataStore.findDriverByNameAndSurname(name, surname);
    }

    public Optional<Driver> findDriverByStartingNumber(Integer number) {
        return dataStore.findDriverByNumber(number);
    }

    public Optional<Driver> findDriverByTeamAndNumber(String teamName, Integer number) {
        return dataStore.findAllDrivers()
                .stream()
                .filter(x -> x.getTeam().getTeamName().equals(teamName) && x.getStartingNumber().equals(number))
                .findFirst();
    }

    public List<Driver> findDriversByTeam(String teamName) {
        return dataStore.findAllDrivers()
                .stream()
                .filter(x -> x.getTeam().getTeamName().equals(teamName))
                .collect(Collectors.toList());
    }

    public void update(Driver driver) {
        dataStore.updateDriver(driver);
    }
}
