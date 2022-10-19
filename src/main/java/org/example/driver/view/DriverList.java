package org.example.driver.view;

import lombok.Data;
import org.example.driver.model.DriversModel;
import org.example.driver.service.DriverService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;


@RequestScoped
@Named
public class DriverList implements Serializable {
    private final DriverService driverService;

    private DriversModel drivers;

    private String teamName;

    @Inject
    public DriverList(DriverService driverService) {
        this.driverService = driverService;
    }

    public DriversModel getDrivers() {
        if(drivers == null) {
            drivers = DriversModel.entityToModelMapper().apply(driverService.findDriversByTeam(teamName));
        }

        return drivers;
    }

public void init() throws IOException {

}
}
