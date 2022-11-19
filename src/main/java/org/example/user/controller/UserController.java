package org.example.user.controller;

import lombok.NoArgsConstructor;
import org.example.user.dto.CreateUserRequest;
import org.example.user.dto.GetUserResponse;
import org.example.user.dto.UpdateUserRequest;
import org.example.user.entity.User;
import org.example.user.service.UserService;

import javax.annotation.security.PermitAll;
import javax.ejb.EJBTransactionRolledbackException;
import javax.inject.Inject;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@NoArgsConstructor
@Path("/users")
@PermitAll
public class UserController {
    private UserService userService;

    private Pbkdf2PasswordHash hash;

    @Inject
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Inject
    public void setHash(Pbkdf2PasswordHash hash) {
        this.hash = hash;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers() {
        List<User> users = userService.findAllUsers();
        List<GetUserResponse> responses = users.stream()
                .map(x -> GetUserResponse.entityToDtoMapper().apply(x))
                .collect(Collectors.toList());

        return Response.ok(responses).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{login}")
    public Response getUser(@PathParam("login") String login) {
        Optional<User> user = userService.findByLogin(login);
        if(user.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(GetUserResponse.entityToDtoMapper().apply(user.get())).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response createUser(CreateUserRequest request) {
        User newUser = CreateUserRequest.dtoToEntityMapper().apply(request);
        newUser.setPassword(hash.generate(newUser.getPassword().toCharArray()));
        try {
            userService.create(newUser);
        } catch (EJBTransactionRolledbackException ex) {
            return Response.status(Response.Status.CONFLICT).build();
        }

        return Response.created(
                UriBuilder.fromMethod(UserController.class, "getUser")
                        .build(request.getLogin())
        ).build();
    }

    @DELETE
    @Path("{login}")
    public Response deleteUser(@PathParam("login") String login) {
        Optional<User> user = userService.findByLogin(login);
        user.ifPresent(value -> userService.deleteUser(value));

        return Response.noContent().build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{login}")
    public Response updateUser(@PathParam("login") String login, UpdateUserRequest request) {
        Optional<User> userOpt = userService.findByLogin(login);
        if(userOpt.isPresent()) {
            //update
            User userFromRequest = UpdateUserRequest.dtoToEntityMapper(hash, login).apply(request);
            userService.updateUser(userFromRequest);
            return Response.ok().build();
        }
        else {
            //create new user
            User user = UpdateUserRequest.dtoToEntityMapper(hash, login).apply(request);
            userService.create(user);
            return Response.created(
                    UriBuilder.fromMethod(UserController.class, "getUser")
                            .build(login)
            ).build();
        }
    }
}
