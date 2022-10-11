package org.example.driver.service;

import java.util.List;
import java.util.Optional;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import lombok.NoArgsConstructor;
import org.example.driver.entity.Driver;
import org.example.driver.repository.DriverRepository;

@ApplicationScoped
@NoArgsConstructor
public class DriverService {
    private DriverRepository driverRepository;

    @Inject
    public DriverService(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    public Optional<Driver> findDriver(Integer number) {
        return driverRepository.findDriverByStartingNumber(number);
    }

    public List<Driver> findAllDrivers() {
        return driverRepository.findAllDrivers();
    }

    public void createDriver(Driver driver) {
        driverRepository.createDriver(driver);
    }

    public void deleteDriver(Driver driver) {
        driverRepository.deleteDriver(driver);
    }

    public void update(Driver driver) {
        driverRepository.update(driver);
    }

    public List<Driver> findDriversByUser(String login) {
        return driverRepository.findDriversByUser(login);
    }

    public Optional<Driver> findDriverByLoginAndNumber(String login, Integer number) {
        return driverRepository.findDriverByUserAndNumber(login, number);
    }

    public List<Driver> findDriversByTeam(String teamName) {
        return driverRepository.findDriversByTeam(teamName);
    }

    public Optional<Driver> findDriverByTeamAndNumber(String teamName, Integer number) {
        return driverRepository.findDriverByTeamAndNumber(teamName, number);
    }
}
