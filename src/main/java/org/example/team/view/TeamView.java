package org.example.team.view;

import lombok.Getter;
import lombok.Setter;
import org.example.driver.dto.GetDriverResponse;
import org.example.driver.model.DriversModel;
import org.example.driver.service.DriverService;
import org.example.team.entity.Team;
import org.example.team.model.TeamModel;
import org.example.team.service.TeamService;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@ViewScoped
@Named
public class TeamView implements Serializable {
    private final TeamService teamService;

    private final DriverService driverService;

    @Getter
    @Setter
    private String name;

    @Getter
    private TeamModel team;

    private DriversModel drivers;

    @Inject
    public TeamView(TeamService teamService, DriverService driverService) {
        this.teamService = teamService;
        this.driverService = driverService;
    }

    public void init() throws IOException {
        Optional<Team> team = teamService.findTeam(name);
        if(team.isPresent()) {
            this.team = TeamModel.entityToModelMapper().apply(team.get());
        } else {
            FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .responseSendError(HttpServletResponse.SC_NOT_FOUND, "Team not found");
        }
    }

    public DriversModel getDrivers() {
        if(drivers == null) {
            drivers = DriversModel.entityToModelMapper().apply(driverService.findDriversByTeam(name));
        }
        return drivers;
    }

    public String deleteDriverAction(GetDriverResponse driver) throws IOException{
        driverService.deleteDriver(driverService.findDriver(driver.getNumber()).orElseThrow());
        return "team_view?faces-redirect=true&includeViewParams=true";
    }
}
