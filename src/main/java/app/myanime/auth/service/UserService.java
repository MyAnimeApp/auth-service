package app.myanime.auth.service;

import app.myanime.auth.model.Group;
import app.myanime.auth.model.User;
import app.myanime.auth.repository.GroupRepository;
import app.myanime.auth.repository.UserRepository;
import app.myanime.core.auth.provider.AuthProvider;
import io.quarkus.runtime.util.HashUtil;
import lombok.Getter;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@ApplicationScoped
@Getter
public class UserService {

    @ConfigProperty(name = "users.auth.token.expiration")
    long expiration;

    @Inject
    UserRepository repository;

    @Inject
    GroupService groupService;

    @Inject
    AuthProvider provider;

    public User create(String name, String password, String mail) {
        User user = new User();
        user.setId(convertNameToId(name));
        user.setName(name);
        user.setMail(mail);
        user.setPassword(hash(password));
        user.setGroups(new HashSet<>());
        repository.persist(user);
        return user;
    }

    public String generateToken(User user) {
        List<String> roles = new ArrayList<>();
        for(String groupName : user.getGroups()) {
            Optional<Group> optional = groupService.getRepository().findByIdOptional(groupName);
            if(optional.isPresent()) {
                Group group = optional.get();
                roles.addAll(group.getPermissions());
            }
        }
        return provider.generateToken(user.getId(), expiration, roles);
    }

    public String convertNameToId(String name) {
        return name.replace(" ", "").toLowerCase();
    }

    public String hash(String input) {
        return HashUtil.sha256(input);
    }
}
