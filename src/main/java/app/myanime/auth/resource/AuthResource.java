package app.myanime.auth.resource;

import app.myanime.auth.model.User;
import app.myanime.auth.model.dto.UserDto;
import app.myanime.auth.repository.UserRepository;
import app.myanime.auth.resource.request.AuthLoginBasicRequest;
import app.myanime.auth.resource.request.AuthLoginTokenRequest;
import app.myanime.auth.resource.request.AuthRegisterRequest;
import app.myanime.auth.resource.response.AuthLoginResponse;
import app.myanime.auth.service.UserService;
import app.myanime.core.auth.provider.AuthResult;
import app.myanime.core.exception.ServiceException;
import io.quarkus.arc.log.LoggerName;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.Optional;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Path("/auth")
public class AuthResource {

    @Inject
    UserRepository repository;

    @Inject
    UserService service;

    @LoggerName("auth")
    Logger logger;

    @Path("/login/basic")
    @POST
    public AuthLoginResponse loginBasic(@Valid AuthLoginBasicRequest request) throws ServiceException {
        Optional<User> optional = repository.findByIdOptional(service.convertNameToId(request.getId()));
        if(!optional.isPresent()) {
            throw new ServiceException(404, "Wrong credentials");
        }
        User user = optional.get();
        if(!user.getPassword().equals(service.hash(request.getPassword()))) {
            throw new ServiceException(404, "Wrong credentials");
        }
        String token = service.generateToken(user);
        return new AuthLoginResponse(new UserDto(user), token);
    }

    @Path("/login/token")
    @POST
    public AuthLoginResponse loginToken(@Valid AuthLoginTokenRequest request) throws ServiceException {
        Optional<AuthResult> authOptional = service.getProvider().auth(request.getToken());
        if(!authOptional.isPresent()) {
            throw new ServiceException(404, "Invalid token");
        }
        AuthResult result = authOptional.get();
        Optional<User> optional = repository.findByIdOptional(result.getUser());
        if(!optional.isPresent()) {
            throw new ServiceException(404, "Wrong credentials");
        }
        User user = optional.get();
        return new AuthLoginResponse(new UserDto(user), request.getToken());
    }

    @Path("/register")
    @POST
    public AuthLoginResponse register(@Valid AuthRegisterRequest request) throws ServiceException {
        if(repository.existsById(service.convertNameToId(request.getName()))) {
            throw new ServiceException(403, "Name already taken");

        }
        if(repository.existsByMail(request.getMail())) {
            throw new ServiceException(403, "Mail already taken");
        }
        User user = service.create(request.getName(), request.getPassword(), request.getMail());
        logger.info("Registered new " + user);
        return new AuthLoginResponse(new UserDto(user), service.generateToken(user));
    }
}
