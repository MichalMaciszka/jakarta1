package org.example.team.servlet;

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
import org.example.driver.dto.CreateDriverForTeamRequest;
import org.example.driver.dto.GetDriverResponse;
import org.example.driver.dto.UpdateDriverRequest;
import org.example.driver.entity.Driver;
import org.example.driver.mapper.DriverMapper;
import org.example.driver.service.DriverService;
import org.example.exception.NotFoundException;
import org.example.team.dto.CreateTeamRequest;
import org.example.team.dto.GetTeamResponse;
import org.example.team.entity.Team;
import org.example.team.service.TeamService;
import org.example.utils.HttpHeaders;
import org.example.utils.MimeTypes;
import org.example.utils.ServletUtility;
import org.example.utils.UrlFactory;

@WebServlet(urlPatterns = "/api/teams/*")
public class TeamServlet extends HttpServlet {
    private final Jsonb jsonb = JsonbBuilder.create();
    private final TeamService teamService;
    private final DriverService driverService;
    private final DriverMapper driverMapper;

    @Inject
    public TeamServlet(TeamService teamService, DriverService driverService, DriverMapper driverMapper) {
        this.teamService = teamService;
        this.driverService = driverService;
        this.driverMapper = driverMapper;
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String parsedPath = ServletUtility.parseRequestPath(req);
        String[] path = parsedPath.split("/");
        long slashesCounter = parsedPath.chars().filter(c -> c == '/').count();
        String teamName = path[1].replace('_', ' ');
        if (slashesCounter != 3) {
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }
        if (!path[2].equals("drivers")) {
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }

        //delete given driver
        Optional<Driver> driver = driverService.findDriverByTeamAndNumber(
                teamName,
                Integer.parseInt(path[3])
        );

        driver.ifPresent(driverService::deleteDriver);
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(MimeTypes.APPLICATION_JSON);
        String parsedPath = ServletUtility.parseRequestPath(req);
        long slashesCounter = parsedPath.chars().filter(c -> c == '/').count();

        if (slashesCounter == 0) {
            //return all teams
            List<GetTeamResponse> teams = teamService.findAllTeams()
                    .stream()
                    .map(x -> GetTeamResponse.entityToDtoMapper().apply(x))
                    .collect(Collectors.toList());
            resp.getWriter().write(jsonb.toJson(teams));
            return;
        } else if (slashesCounter == 1) {
            //return specific team
            String teamName = parsedPath.split("/")[1].replace('_', ' ');
            Optional<Team> team = teamService.findTeam(teamName);
            if (team.isEmpty()) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            resp.getWriter().write(jsonb.toJson(
                    GetTeamResponse.entityToDtoMapper().apply(team.get())
            ));
            return;
        } else if (slashesCounter == 2) {
            //check if path[2] == "drivers", return all drivers from team
            String[] path = parsedPath.split("/");
            String teamName = path[1].replace('_', ' ');
            if (!path[2].equals("drivers")) {
                resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                return;
            }
            List<GetDriverResponse> drivers = driverService.findDriversByTeam(teamName)
                    .stream()
                    .map(driver -> GetDriverResponse.entityToDtoMapper().apply(driver))
                    .collect(Collectors.toList());
            resp.getWriter().write(jsonb.toJson(drivers));
            return;
        } else if (slashesCounter == 3) {
            //check if path[2] == "drivers", return driver with specific number
            String[] path = parsedPath.split("/");
            String teamName = path[1].replace('_', ' ');
            Integer number = Integer.parseInt(path[3]);
            if (!path[2].equals("drivers")) {
                resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                return;
            }

            Optional<Driver> driver = driverService.findDriverByTeamAndNumber(teamName, number);
            if (driver.isEmpty()) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            resp.getWriter().write(jsonb.toJson(
                    GetDriverResponse.entityToDtoMapper().apply(driver.get())
            ));
            return;
        }
        resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String parsedPath = ServletUtility.parseRequestPath(req);
        long slashesCounter = parsedPath.chars().filter(c -> c == '/').count();

        if (slashesCounter == 0) {
            //add team logic
            CreateTeamRequest requestBody = jsonb.fromJson(
                    req.getInputStream(),
                    CreateTeamRequest.class
            );
            Team team = CreateTeamRequest.dtoToEntityMapper().apply(requestBody);
            try {
                teamService.createTeam(team);
                resp.addHeader(HttpHeaders.LOCATION,
                        UrlFactory.createUrl(
                                req,
                                "/api/drivers",
                                team.getTeamName().replace(' ', '_')
                        )
                );
                resp.setStatus(HttpServletResponse.SC_CREATED);
                return;
            } catch (IllegalArgumentException ex) {
                resp.sendError(HttpServletResponse.SC_CONFLICT);
                return;
            }
        } else if (slashesCounter == 2) {
            //check if path[2] == "drivers", add driver to specific team
            String[] path = parsedPath.split("/");
            String teamName = path[1].replace('_', ' ');
            if (!path[2].equals("drivers")) {
                resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                return;
            }

            try {
                CreateDriverForTeamRequest requestBody = jsonb.fromJson(
                        req.getInputStream(),
                        CreateDriverForTeamRequest.class
                );
                Driver driver = driverMapper.createDriverForTeamMapper(requestBody, teamName);
                driverService.createDriver(driver);
                resp.addHeader(HttpHeaders.LOCATION,
                        UrlFactory.createUrl(
                                req,
                                "/api/teams",
                                path[1],
                                "/drivers",
                                requestBody.getStartingNumber()
                        )
                );
                resp.setStatus(HttpServletResponse.SC_CREATED);
                return;
            } catch (NotFoundException nfe) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            } catch (IllegalStateException ise) {
                resp.sendError(HttpServletResponse.SC_CONFLICT);
                return;
            } catch (IllegalArgumentException iae) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

        }

        resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UpdateDriverRequest requestBody = jsonb.fromJson(
                req.getInputStream(),
                UpdateDriverRequest.class
        );

        String parsedPath = ServletUtility.parseRequestPath(req);
        String[] path = parsedPath.split("/");
        String teamName = path[1].replace('_', ' ');
        long slashesCounter = parsedPath.chars().filter(c -> c == '/').count();
        if (slashesCounter != 3) {
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }
        if (!path[2].equals("drivers")) {
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }

        //update given driver
        Optional<Driver> opt = driverService.findDriverByTeamAndNumber(
                teamName,
                Integer.parseInt(path[3])
        );
        if (opt.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Driver driver = opt.get();
        driver.setName(requestBody.getName());
        driver.setSurname(requestBody.getSurname());
        driver.setName(requestBody.getNationality());
        driver.setRacesWon(Integer.parseInt(requestBody.getRacesWon()));
        try {
            driverService.update(driver);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (IllegalArgumentException ex) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
