package app.myanime.auth.model.dto;

import app.myanime.auth.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private String id;

    private String name;
    private String mail;

    private Set<String> groups;

    public UserDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.mail = user.getMail();
        this.groups = user.getGroups();
    }
}
