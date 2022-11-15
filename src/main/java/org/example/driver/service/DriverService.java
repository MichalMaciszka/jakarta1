package org.example.driver.service;

import lombok.NoArgsConstructor;
import org.example.driver.entity.Driver;
import org.example.driver.repository.DriverRepository;
import org.example.team.repository.TeamRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.RollbackException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
@NoArgsConstructor
public class DriverService {
    private DriverRepository driverRepository;

    private TeamRepository teamRepository;

    @Inject
    public DriverService(DriverRepository driverRepository, TeamRepository teamRepository) {
        this.driverRepository = driverRepository;
        this.teamRepository = teamRepository;
    }

    @Transactional
    public void createDriver(Driver driver) throws RollbackException {
        driverRepository.createDriver(driver);
        teamRepository.findTeam(driver.getTeam().getTeamName()).ifPresent(team -> team.getDrivers().add(driver));
    }

    @Transactional
    public void deleteDriver(Driver driver) {
        Driver original = driverRepository.findDriverByStartingNumber(driver.getStartingNumber()).orElseThrow();
        original.getTeam().getDrivers().remove(original);
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

    @Transactional
    public void update(Driver driver) {
        Driver original = driverRepository.findDriverByStartingNumber(driver.getStartingNumber()).orElseThrow();
        driverRepository.detach(original);
        if(!original.getTeam().getTeamName().equals(driver.getTeam().getTeamName())) {
            original.getTeam().getDrivers().removeIf(toRemove -> toRemove.getStartingNumber().equals(driver.getStartingNumber()));
            teamRepository.findTeam(driver.getTeam().getTeamName()).ifPresent(team -> team.getDrivers().add(driver));
        }
        driverRepository.update(driver);
    }
}
