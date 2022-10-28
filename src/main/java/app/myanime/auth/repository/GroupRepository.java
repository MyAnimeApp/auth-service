package app.myanime.auth.repository;

import app.myanime.auth.model.Group;
import app.myanime.auth.service.GroupService;
import io.quarkus.mongodb.panache.PanacheMongoRepositoryBase;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@ApplicationScoped
public class GroupRepository implements PanacheMongoRepositoryBase<Group, String> {

    @Inject
    GroupService service;


    public boolean existsById(String id) {
        return findByIdOptional(service.convertToId(id)).isPresent();
    }
}
