package org.example.driver.service;

import lombok.NoArgsConstructor;
import org.example.driver.entity.Driver;
import org.example.driver.repository.DriverRepository;
import org.example.team.repository.TeamRepository;
import org.example.user.entity.User;
import org.example.user.entity.UserRole;
import org.example.user.repository.UserRepository;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJBAccessException;
import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
import java.util.List;
import java.util.Optional;

@Stateless
@LocalBean
@NoArgsConstructor
public class DriverService {
    private DriverRepository driverRepository;

    private TeamRepository teamRepository;

    private UserRepository userRepository;

    private SecurityContext securityContext;

    @Inject
    public DriverService(DriverRepository driverRepository, TeamRepository teamRepository, UserRepository userRepository, SecurityContext securityContext) {
        this.driverRepository = driverRepository;
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
        this.securityContext = securityContext;
    }

    @RolesAllowed(UserRole.USER)
    public void createDriver(Driver driver) throws EJBTransactionRolledbackException {
        User caller = userRepository.findUser(securityContext.getCallerPrincipal().getName()).orElseThrow();
        driver.setUser(caller);
        driverRepository.createDriver(driver);
        teamRepository.findTeam(driver.getTeam().getTeamName()).ifPresent(team -> team.getDrivers().add(driver));
    }

    @RolesAllowed({UserRole.USER, UserRole.ADMIN})
    public void deleteDriver(Driver driver) {
        User currentUser = userRepository.findUser(securityContext.getCallerPrincipal().getName()).orElseThrow(EJBAccessException::new);
        if(driver.getUser().equals(currentUser) || currentUser.getRoles().contains(UserRole.ADMIN)) {
            Driver original = driverRepository.findDriverByStartingNumber(driver.getStartingNumber()).orElseThrow();
            original.getTeam().getDrivers().remove(original);
            driverRepository.deleteDriver(driver);
        } else {
            throw new EJBAccessException("FORBIDDEN");
        }
    }

    public List<Driver> findAllDrivers() {
        return driverRepository.findAllDrivers();
    }

    @RolesAllowed({UserRole.USER, UserRole.ADMIN})
    public Optional<Driver> findDriver(Integer number) {
        User caller = userRepository.findUser(securityContext.getCallerPrincipal().getName()).orElseThrow(EJBAccessException::new);
        if(caller.getRoles().contains(UserRole.ADMIN)) {
            return driverRepository.findDriverByStartingNumber(number);
        } else {
            return driverRepository.findDriverByStartingNumberAndUser(number, caller);
        }
    }

    @RolesAllowed({UserRole.USER, UserRole.ADMIN})
    public Optional<Driver> findDriverByTeamAndNumber(String teamName, Integer number) {
        User caller = userRepository.findUser(securityContext.getCallerPrincipal().getName()).orElseThrow(EJBAccessException::new);
        if(caller.getRoles().contains(UserRole.ADMIN)) {
            return driverRepository.findDriverByTeamAndNumber(teamName, number);
        } else {
            return driverRepository.findDriverByUserTeamAndNumber(
                    caller,
                    teamRepository.findTeam(teamName).orElseThrow(),
                    number
            );
        }
    }

    @RolesAllowed({UserRole.USER, UserRole.ADMIN})
    public List<Driver> findDriversByTeam(String teamName) {
        User caller = userRepository.findUser(securityContext.getCallerPrincipal().getName()).orElseThrow(EJBAccessException::new);
        if(caller.getRoles().contains(UserRole.ADMIN)) {
            return driverRepository.findDriversByTeam(teamName);
        } else {
            return driverRepository.findAllByTeamAndUser(teamRepository.findTeam(teamName).orElseThrow(), caller);
        }
    }

    @RolesAllowed({UserRole.USER, UserRole.ADMIN})
    public void update(Driver driver) {
        //if current user is admin OR if current user is owner of element
        User currentUser = userRepository.findUser(securityContext.getCallerPrincipal().getName()).orElseThrow(EJBAccessException::new);
        if(driver.getUser().equals(currentUser) || currentUser.getRoles().contains(UserRole.ADMIN)) {
            Driver original = driverRepository.findDriverByStartingNumber(driver.getStartingNumber()).orElseThrow();
            driverRepository.detach(original);
            if (!original.getTeam().getTeamName().equals(driver.getTeam().getTeamName())) {
                original.getTeam().getDrivers().removeIf(toRemove -> toRemove.getStartingNumber().equals(driver.getStartingNumber()));
                teamRepository.findTeam(driver.getTeam().getTeamName()).ifPresent(team -> team.getDrivers().add(driver));
            }
            driverRepository.update(driver);
        } else {
            throw new EJBAccessException("FORBIDDEN");
        }
    }
}
