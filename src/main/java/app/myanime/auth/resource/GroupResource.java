package app.myanime.auth.resource;

import app.myanime.auth.model.Group;
import app.myanime.auth.repository.GroupRepository;
import app.myanime.auth.service.GroupService;
import app.myanime.core.ValidationGroups;
import app.myanime.core.exception.ServiceException;
import io.quarkus.arc.log.LoggerName;
import org.jboss.logging.Logger;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.groups.ConvertGroup;
import javax.ws.rs.*;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Path("/groups")
public class GroupResource {

    @Inject
    GroupRepository repository;

    @Inject
    GroupService service;

    @LoggerName("group")
    Logger logger;

    @GET
    @RolesAllowed("groups.get.all")
    public List<Group> getAll() {
        return repository.listAll();
    }

    @GET
    @RolesAllowed("groups.get")
    @Path("/{id}")
    public Group get(@PathParam("id") String id) throws ServiceException {
        Optional<Group> optional = repository.findByIdOptional(service.convertToId(id));
        if(!optional.isPresent()) {
            throw new ServiceException(404, "Group not found");
        }
        return optional.get();
    }

    @POST
    @RolesAllowed("groups.create")
    public Group create(@Valid @ConvertGroup(to = ValidationGroups.Post.class) Group group) throws ServiceException {
        if(repository.existsById(service.convertToId(group.getName()))) {
            throw new ServiceException(409, "Name already taken");
        }
        logger.info("Created new " + group.toString());
        return service.create(group.getName());
    }

    @PATCH
    @Path("/{id}")
    @RolesAllowed("groups.update")
    public Group update(@PathParam("id") String id, Group group) throws ServiceException {
        Optional<Group> optional = repository.findByIdOptional(service.convertToId(id));
        if(!optional.isPresent()) {
            throw new ServiceException(404, "Group not found");
        }
        Group persistGroup = optional.get();
        if(group.getName() != null) {
            persistGroup.setName(group.getName());
        }
        if(group.getPermissions() != null) {
            persistGroup.setPermissions(group.getPermissions());
        }
        repository.update(persistGroup);
        logger.info("Updated " + persistGroup);
        return persistGroup;
    }

    @DELETE
    @RolesAllowed("groups.delete")
    @Path("/{id}")
    public Group delete(@PathParam("id") String id) throws ServiceException {
        Optional<Group> optional = repository.findByIdOptional(service.convertToId(id));
        if(!optional.isPresent()) {
            throw new ServiceException(404, "Group not found");
        }
        Group group = optional.get();
        repository.delete(group);
        logger.info("Deleted " + group);
        return group;
    }
}
