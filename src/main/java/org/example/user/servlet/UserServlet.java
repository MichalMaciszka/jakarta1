package org.example.user.servlet;

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
import org.example.driver.dto.CreateDriverForUserRequest;
import org.example.driver.dto.GetDriverResponse;
import org.example.driver.dto.UpdateDriverRequest;
import org.example.driver.entity.Driver;
import org.example.driver.mapper.DriverMapper;
import org.example.driver.service.DriverService;
import org.example.exception.NotFoundException;
import org.example.user.dto.CreateUserRequest;
import org.example.user.dto.GetUserResponse;
import org.example.user.entity.User;
import org.example.utils.HttpHeaders;
import org.example.user.service.UserService;
import org.example.utils.MimeTypes;
import org.example.utils.ServletUtility;
import org.example.utils.UrlFactory;

@WebServlet(urlPatterns = "/api/users/*")
public class UserServlet extends HttpServlet {
    private final Jsonb jsonb = JsonbBuilder.create();
    private final UserService userService;
    private final DriverService driverService;
    private final DriverMapper driverMapper;

    @Inject
    public UserServlet(UserService userService, DriverService driverService, DriverMapper driverMapper) {
        this.userService = userService;
        this.driverService = driverService;
        this.driverMapper = driverMapper;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType(MimeTypes.APPLICATION_JSON);

        String parsedPath = ServletUtility.parseRequestPath(req);
        System.out.println(parsedPath);
        long slashesCounter = parsedPath.chars().filter(c -> c == '/').count();
        if(slashesCounter == 0) {
            //return List of all users
            List<GetUserResponse> list = userService.findAllUsers()
                    .stream()
                    .map(user -> GetUserResponse.entityToDtoMapper().apply(user))
                    .collect(Collectors.toList());
            resp.getWriter().write(jsonb.toJson(list));
        }
        else if(slashesCounter == 1) {
            //return one user
            String username = ServletUtility.parseRequestPath(req).split("/")[1];
            Optional<User> result = userService.findByLogin(username);
            if(result.isEmpty()){
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            resp.getWriter().write(jsonb.toJson(GetUserResponse.entityToDtoMapper().apply(result.get())));
        }
        else if(slashesCounter == 2) {
            //check if last part == drivers, return drivers by user
            if(!ServletUtility.parseRequestPath(req).split("/")[2].equals("drivers")) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            String username = ServletUtility.parseRequestPath(req).split("/")[1];
            List<GetDriverResponse> drivers = driverService.findDriversByUser(username)
                    .stream()
                    .map(item -> GetDriverResponse.entityToDtoMapper().apply(item))
                    .collect(Collectors.toList());

            resp.getWriter().write(jsonb.toJson(drivers));
        }
        else if(slashesCounter == 3) {
            //return one specific Driver from User's collection
            if(!ServletUtility.parseRequestPath(req).split("/")[2].equals("drivers")) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            String username = ServletUtility.parseRequestPath(req).split("/")[1];
            Integer number = Integer.parseInt(ServletUtility.parseRequestPath(req).split("/")[3]);
            Optional<Driver> driver = driverService.findDriverByLoginAndNumber(username, number);
            if(driver.isPresent()) {
                resp.getWriter().write(jsonb.toJson(GetDriverResponse.entityToDtoMapper().apply(driver.get())));
                return;
            }

            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
        else {
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String parsedPath = ServletUtility.parseRequestPath(req);
        long slashesCounter = parsedPath.chars().filter(c -> c == '/').count();
        if(slashesCounter == 0) {
            addUser(req, resp);
        } else if(slashesCounter == 2){
            //add driver
            addDriverWithSpecificUser(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String parsedPath = ServletUtility.parseRequestPath(req);
        long slashesCounter = parsedPath.chars().filter(c -> c == '/').count();

        if(slashesCounter != 3) {
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }

        Optional<Driver> driver =
                driverService.findDriverByLoginAndNumber(parsedPath.split("/")[1],
                        Integer.parseInt(parsedPath.split("/")[3]));

        driver.ifPresent(driverService::deleteDriver);
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UpdateDriverRequest requestBody = jsonb.fromJson(
                req.getInputStream(),
                UpdateDriverRequest.class
        );

        String parsedPath = ServletUtility.parseRequestPath(req);
        long slashesCounter = parsedPath.chars().filter(c -> c == '/').count();

        if(slashesCounter != 3) {
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }

        Driver driver =
                driverService.findDriverByLoginAndNumber(parsedPath.split("/")[1],
                        Integer.parseInt(parsedPath.split("/")[3])).orElseThrow();
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

    private void addUser(HttpServletRequest req, HttpServletResponse resp)throws IOException {
        CreateUserRequest requestBody = jsonb.fromJson(
                req.getInputStream(),
                CreateUserRequest.class
        );
        User user = CreateUserRequest.dtoToEntityMapper().apply(requestBody);
        try {
            userService.create(user);
            resp.addHeader(HttpHeaders.LOCATION,
                    UrlFactory.createUrl(
                            req,
                            "/api/users",
                            user.getLogin()
                    )
            );
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (IllegalArgumentException ex) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private synchronized void addDriverWithSpecificUser(HttpServletRequest request,
                                           HttpServletResponse response) throws IOException{
        String[] path = ServletUtility.parseRequestPath(request).split("/");
        try {
            CreateDriverForUserRequest requestBody = jsonb.fromJson(
                    request.getInputStream(),
                    CreateDriverForUserRequest.class
            );
            Driver driver = driverMapper.createDriverForUserMapping(
                    requestBody,
                    path[1]
            );
            driverService.createDriver(driver);
            response.addHeader(
                    HttpHeaders.LOCATION,
                    UrlFactory.createUrl(
                            request,
                            "/api/users",
                            path[1],
                            "/drivers",
                            requestBody.getStartingNumber().toString()
                    )
            );
            response.setStatus(HttpServletResponse.SC_CREATED);
        } catch (NotFoundException nfe) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        } catch (IllegalStateException ise) {
            response.sendError(HttpServletResponse.SC_CONFLICT);
        } catch (IllegalArgumentException iae) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
