package org.example.driver.service;

import lombok.NoArgsConstructor;
import org.example.driver.entity.Driver;
import org.example.driver.repository.DriverRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
@NoArgsConstructor
public class DriverService {
    private DriverRepository driverRepository;

    @Inject
    public DriverService(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    public void createDriver(Driver driver) {
        driverRepository.createDriver(driver);
    }

    public void deleteDriver(Driver driver) {
        driverRepository.deleteDriver(driver);
    }

    public List<Driver> findAllDrivers() {
        return driverRepository.findAllDrivers();
    }

    public Optional<Driver> findDriver(Integer number) {
        return driverRepository.findDriverByStartingNumber(number);
    }

    public Optional<Driver> findDriverByTeamAndNumber(String teamName, Integer number) {
        return driverRepository.findDriverByTeamAndNumber(teamName, number);
    }

    public List<Driver> findDriversByTeam(String teamName) {
        return driverRepository.findDriversByTeam(teamName);
    }

    public void update(Driver driver) {
        driverRepository.update(driver);
    }
}
