package org.example.driver.view;

import lombok.Getter;
import lombok.Setter;
import org.example.driver.entity.Driver;
import org.example.driver.model.DriverModel;
import org.example.driver.service.DriverService;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.Optional;

@ViewScoped
@Named
public class DriverView implements Serializable {
    private final DriverService driverService;

    @Getter
    @Setter
    private Integer number;

    @Getter
    private DriverModel driver;

    @Inject
    public DriverView(DriverService driverService) {
        this.driverService = driverService;
    }

    public void init() throws IOException {
        Optional<Driver> opt = driverService.findDriver(number);
        if(opt.isPresent()) {
            this.driver = DriverModel.entityToModelMapper().apply(opt.get());
        } else {
            FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .responseSendError(HttpServletResponse.SC_NOT_FOUND, "driver not found");
        }
    }
}
