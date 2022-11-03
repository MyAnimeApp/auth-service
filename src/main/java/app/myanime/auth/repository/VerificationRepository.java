package app.myanime.auth.repository;

import app.myanime.auth.model.User;
import app.myanime.auth.model.Verification;
import io.quarkus.mongodb.panache.PanacheMongoRepositoryBase;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@ApplicationScoped
public class VerificationRepository implements PanacheMongoRepositoryBase<Verification, String> {

    public boolean existsById(String id) {
        return findByIdOptional(id).isPresent();
    }

    public Optional<Verification> getByUser(String user) {
        return find("user", user).firstResultOptional();
    }

    public boolean existsByUser(String user) {
        return getByUser(user).isPresent();
    }

}
