package org.example.portrait.servlet;

import org.example.exception.NotFoundException;
import org.example.utils.PortraitUtils;
import org.example.user.entity.User;
import org.example.user.service.UserService;
import org.example.utils.HttpHeaders;
import org.example.utils.MimeTypes;
import org.example.utils.ServletUtility;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.Optional;

@WebServlet(urlPatterns = "/api/portrait/*")
@MultipartConfig(maxFileSize = 200 * 1024)
public class PortraitServlet extends HttpServlet {
    private final UserService userService;

    @Inject
    public PortraitServlet(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String filePath = getServletContext().getInitParameter("imageSavePath");
        String path = ServletUtility.parseRequestPath(req);
        String login = path.split("/")[1];
        Optional<User> user = userService.findByLogin(login);
        if(user.isPresent()) {
            try {
                byte[] image = PortraitUtils.getImage(filePath, login);
                resp.addHeader(HttpHeaders.CONTENT_TYPE, MimeTypes.IMAGE_PNG);
                resp.setContentLength(image.length);
                resp.getOutputStream().write(image);

            } catch (NotFoundException ex) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String filePath = getServletContext().getInitParameter("imageSavePath");
        String path = ServletUtility.parseRequestPath(req);
        String login = path.split("/")[1];
        Optional<User> user = userService.findByLogin(login);
        if(user.isPresent()) {
            boolean fileExists = PortraitUtils.isPresent(filePath, login);
            Part portrait = req.getPart("portrait");
            userService.updatePortrait(filePath, login, portrait.getInputStream().readAllBytes());
            if(fileExists) {
                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                resp.setStatus(HttpServletResponse.SC_CREATED);
            }
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String filePath = getServletContext().getInitParameter("imageSavePath");
        String path = ServletUtility.parseRequestPath(req);
        String login = path.split("/")[1];
        Optional<User> user = userService.findByLogin(login);
        if(user.isPresent()) {
            Part portrait = req.getPart("portrait");
            if (PortraitUtils.isPresent(filePath, login)) {
                resp.sendError(HttpServletResponse.SC_CONFLICT);
            } else {
//                PortraitUtils.saveImage(filePath, login, portrait.getInputStream().readAllBytes());
                userService.updatePortrait(filePath, login, portrait.getInputStream().readAllBytes());
                resp.addHeader(HttpHeaders.LOCATION, "/api/portrait/" + login);
                resp.setStatus(HttpServletResponse.SC_CREATED);
            }
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = ServletUtility.parseRequestPath(req);
        long slashesCounter = path.chars().filter(c -> c == '/').count();
        if (slashesCounter != 1) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String login = path.split("/")[1];
        String filePath = getServletContext().getInitParameter("imageSavePath");

        userService.deletePortrait(filePath, login);
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
