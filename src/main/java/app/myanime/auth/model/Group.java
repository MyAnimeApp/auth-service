package app.myanime.auth.model;

import app.myanime.core.ValidationGroups;
import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.Data;
import org.bson.codecs.pojo.annotations.BsonId;

import javax.validation.constraints.NotBlank;
import java.util.Set;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/

@Data
@MongoEntity(collection = "groups")
public class Group {

    @BsonId
    private String id;

    @NotBlank(groups = {ValidationGroups.Post.class})
    private String name;

    private Set<String> permissions;


}
