package org.example.driver.servlet;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.example.driver.dto.CreateDriverRequest;
import org.example.driver.dto.GetDriverResponse;
import org.example.driver.dto.UpdateDriverRequest;
import org.example.driver.entity.Driver;
import org.example.driver.mapper.DriverMapper;
import org.example.driver.service.DriverService;
import org.example.exception.NotFoundException;
import org.example.utils.HttpHeaders;
import org.example.utils.MimeTypes;
import org.example.utils.ServletUtility;
import org.example.utils.UrlFactory;

@WebServlet(urlPatterns = "/api/drivers/*")
public class DriverServlet extends HttpServlet {
    private final DriverService driverService;
    private final DriverMapper driverMapper;
    private final Jsonb jsonb = JsonbBuilder.create();

    @Inject
    public DriverServlet(DriverService driverService, DriverMapper driverMapper) {
        this.driverService = driverService;
        this.driverMapper = driverMapper;
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String parsedPath = ServletUtility.parseRequestPath(req);
        long slashesCounter = parsedPath.chars().filter(c -> c == '/').count();
        if (slashesCounter != 1) {
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }

        Integer number = Integer.parseInt(parsedPath.split("/")[1]);

        Optional<Driver> driver = driverService.findDriver(number);

        if (driver.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            return;
        }

        driverService.deleteDriver(driver.get());
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(MimeTypes.APPLICATION_JSON);

        String parsedPath = ServletUtility.parseRequestPath(req);
        System.out.println(parsedPath);
        long slashesCounter = parsedPath.chars().filter(c -> c == '/').count();

        if (slashesCounter == 0) {
            //return all drivers
            List<GetDriverResponse> drivers = driverService.findAllDrivers()
                    .stream()
                    .map(item -> GetDriverResponse.entityToDtoMapper().apply(item))
                    .collect(Collectors.toList());
            resp.getWriter().write(jsonb.toJson(drivers));
            return;
        }
        if (slashesCounter == 1) {
            //return specific driver
            Integer number = Integer.parseInt(
                    parsedPath.split("/")[1]
            );
            Optional<Driver> driver = driverService.findDriver(number);
            if (driver.isEmpty()) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            resp.getWriter().write(jsonb.toJson(
                    GetDriverResponse.entityToDtoMapper().apply(driver.get())
            ));
            return;
        }
        resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String parsedPath = ServletUtility.parseRequestPath(req);
        System.out.println(parsedPath);
        long slashesCounter = parsedPath.chars().filter(c -> c == '/').count();
        if (slashesCounter != 0) {
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }
        try {
            CreateDriverRequest bodyRequest = jsonb.fromJson(
                    req.getInputStream(),
                    CreateDriverRequest.class
            );

            Driver driver = driverMapper.createDriverMapping(bodyRequest);
            driverService.createDriver(driver);
            resp.addHeader(HttpHeaders.LOCATION,
                    UrlFactory.createUrl(
                            req,
                            "/api/drivers",
                            bodyRequest.getStartingNumber()
                    )
            );
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (NotFoundException nfe) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        } catch (IllegalStateException ise) {
            resp.sendError(HttpServletResponse.SC_CONFLICT);
        } catch (IllegalArgumentException iae) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UpdateDriverRequest requestBody = jsonb.fromJson(
                req.getInputStream(),
                UpdateDriverRequest.class
        );

        String parsedPath = ServletUtility.parseRequestPath(req);
        long slashesCounter = parsedPath.chars().filter(c -> c == '/').count();

        if (slashesCounter != 1) {
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }

        Integer number = Integer.parseInt(parsedPath.split("/")[1]);
        Optional<Driver> opt = driverService.findDriver(number);

        if (opt.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Driver driver = opt.get();
        driver.setName(requestBody.getName());
        driver.setSurname(requestBody.getSurname());
        driver.setNationality(requestBody.getNationality());
        driver.setRacesWon(Integer.parseInt(requestBody.getRacesWon()));

        try {
            driverService.update(driver);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (IllegalArgumentException ex) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
