package org.example.driver.view;

import lombok.Getter;
import lombok.Setter;
import org.example.driver.entity.Driver;
import org.example.driver.model.DriverEditModel;
import org.example.driver.service.DriverService;
import org.example.team.model.TeamModel;
import org.example.team.service.TeamService;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ViewScoped
@Named
public class DriverEdit implements Serializable {
    private final DriverService driverService;
    private final TeamService teamService;

    @Getter
    @Setter
    private Integer startingNumber;

    @Getter
    private DriverEditModel driverEditModel;

    @Getter
    private List<TeamModel> teams;

    @Inject
    public DriverEdit(DriverService driverService, TeamService teamService) {
        this.driverService = driverService;
        this.teamService = teamService;
    }

    public void init() throws IOException {
        Optional<Driver> driver = driverService.findDriver(startingNumber);
        if(driver.isPresent()) {
            teams = teamService
                    .findAllTeams()
                    .stream()
                    .map(x -> TeamModel.entityToModelMapper().apply(x))
                    .collect(Collectors.toList());
            driverEditModel = DriverEditModel.entityToModelMapper().apply(driver.get());
        } else {
            FacesContext.getCurrentInstance().getExternalContext()
                    .responseSendError(HttpServletResponse.SC_NOT_FOUND, "Driver not found");
        }
    }

    public String saveAction() {
        Driver driver = driverService.findDriver(startingNumber).orElseThrow();

        driver = DriverEditModel.updater(x -> teamService.findTeam(x.getName()).orElseThrow()).apply(driver, driverEditModel);
        driverService.update(driver);
        return "/drivers/driver_view?number=" + driver.getStartingNumber().toString() + "&faces-redirect=true&includeViewParams=true";
    }
}
