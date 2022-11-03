package app.myanime.auth.model;

import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonId;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@MongoEntity(collection = "verification_codes")
@Data
public class Verification {

    @BsonId
    private String id;
    private String user;
    private long timestamp;
}
