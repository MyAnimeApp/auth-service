package app.myanime.auth.repository;

import app.myanime.auth.model.Group;
import io.quarkus.mongodb.panache.PanacheMongoRepositoryBase;

import javax.enterprise.context.ApplicationScoped;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@ApplicationScoped
public class GroupRepository implements PanacheMongoRepositoryBase<Group, String> {

    public boolean existsById(String id) {
        return findByIdOptional(id).isPresent();
    }
}
