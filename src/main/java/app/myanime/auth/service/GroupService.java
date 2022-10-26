package app.myanime.auth.service;

import app.myanime.auth.model.Group;
import app.myanime.auth.repository.GroupRepository;
import io.quarkus.runtime.StartupEvent;
import lombok.Getter;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.HashSet;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@ApplicationScoped
@Getter
public class GroupService {

    @Inject
    GroupRepository repository;

    @ConfigProperty(name = "groups.default.user")
    String defaultUserName;

    @ConfigProperty(name = "groups.default.admin")
    String defaultAdminName;

    Group defaultUserGroup;
    Group defaultAdminGroup;

    private void onStart(@Observes StartupEvent event) {
        if(repository.existsById(convertNameToId(defaultUserName))) {
            defaultUserGroup = repository.findById(convertNameToId(defaultUserName));
        } else {
            defaultUserGroup = create(defaultUserName);
        }
        if(repository.existsById(convertNameToId(defaultAdminName))) {
            defaultAdminGroup = repository.findById(convertNameToId(defaultAdminName));
        } else {
            defaultAdminGroup = create(defaultAdminName, "*");
        }
    }

    public Group create(String name, String... permissions) {
        Group group = new Group();
        group.setId(convertNameToId(name));
        group.setName(name);
        group.setPermissions(new HashSet<>(Arrays.asList(permissions)));
        repository.persist(group);
        return group;
    }

    public String convertNameToId(String name) {
        return name.replace(" ", "").toLowerCase();
    }
}
