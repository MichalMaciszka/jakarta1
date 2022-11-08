package org.example.driver.view;

import lombok.Getter;
import lombok.Setter;
import org.example.driver.model.DriverCreateModel;
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
import java.util.stream.Collectors;


@ViewScoped
@Named
public class DriverCreate implements Serializable {
    private final DriverService driverService;
    private final TeamService teamService;

    @Getter
    @Setter
    private String defaultTeam;

    @Getter
    private final DriverCreateModel driverCreateModel = new DriverCreateModel();

    @Getter
    private List<TeamModel> teams;

    @Inject
    public DriverCreate(DriverService driverService, TeamService teamService) {
        this.driverService = driverService;
        this.teamService = teamService;
    }

    public void init() {
        teams = teamService
                .findAllTeams()
                .stream()
                .map(x -> TeamModel.entityToModelMapper().apply(x))
                .collect(Collectors.toList());
        var team = teamService.findTeam(defaultTeam);
        team.ifPresent(value -> driverCreateModel.setTeamModel(TeamModel.entityToModelMapper().apply(value)));
    }

    public String addAction() throws IOException {
        var driver = driverService.findDriver(driverCreateModel.getStartingNumber());
        if (driver.isPresent()) {
            FacesContext.getCurrentInstance().getExternalContext()
                    .responseSendError(HttpServletResponse.SC_CONFLICT, "invalid number");
            return "";
        }

        var toCreate = DriverCreateModel
                .modelToEntityMapper(model -> teamService.findTeam(model.getName()).orElseThrow())
                .apply(driverCreateModel);
        driverService.createDriver(toCreate);
        return "/drivers/driver_view?number=" + toCreate.getStartingNumber().toString() + "&faces-redirect=true&includeViewParams=true";
    }
}
