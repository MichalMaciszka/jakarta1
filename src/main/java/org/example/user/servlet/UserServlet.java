package org.example.user.servlet;

import  org.example.user.dto.CreateUserRequest;
import org.example.user.dto.GetUserResponse;
import org.example.user.entity.User;
import org.example.user.service.UserService;
import org.example.utils.HttpHeaders;
import org.example.utils.MimeTypes;
import org.example.utils.ServletUtility;
import org.example.utils.UrlFactory;

import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.RollbackException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = "/api/users/*")
public class UserServlet extends HttpServlet {
    private final Jsonb jsonb = JsonbBuilder.create();
    private final UserService userService;

    @Inject
    public UserServlet(UserService userService) {
        this.userService = userService;
    }

    private void addUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
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
        } catch (RollbackException ex) {
            resp.sendError(HttpServletResponse.SC_CONFLICT);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType(MimeTypes.APPLICATION_JSON);

        String parsedPath = ServletUtility.parseRequestPath(req);
        long slashesCounter = parsedPath.chars().filter(c -> c == '/').count();
        if (slashesCounter == 0) {
            //return List of all users
            List<GetUserResponse> list = userService.findAllUsers()
                    .stream()
                    .map(user -> GetUserResponse.entityToDtoMapper().apply(user))
                    .collect(Collectors.toList());
            resp.getWriter().write(jsonb.toJson(list));
        } else if (slashesCounter == 1) {
            //return one user
            String username = ServletUtility.parseRequestPath(req).split("/")[1];
            Optional<User> result = userService.findByLogin(username);
            if (result.isEmpty()) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            resp.getWriter().write(jsonb.toJson(GetUserResponse.entityToDtoMapper().apply(result.get())));
        } else {
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String parsedPath = ServletUtility.parseRequestPath(req);
        long slashesCounter = parsedPath.chars().filter(c -> c == '/').count();
        if (slashesCounter == 0) {
            addUser(req, resp);
            return;
        }
        resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }
}
