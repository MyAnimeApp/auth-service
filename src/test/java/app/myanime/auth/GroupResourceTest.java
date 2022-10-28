package app.myanime.auth;

import app.myanime.auth.model.Group;
import app.myanime.auth.model.User;
import app.myanime.auth.repository.GroupRepository;
import app.myanime.auth.resource.AuthResource;
import app.myanime.auth.resource.GroupResource;
import app.myanime.auth.resource.request.AuthLoginBasicRequest;
import app.myanime.auth.resource.request.AuthLoginTokenRequest;
import app.myanime.auth.resource.request.AuthRegisterRequest;
import app.myanime.auth.service.GroupService;
import app.myanime.auth.service.UserService;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import java.util.Arrays;
import java.util.HashSet;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@QuarkusTest
@TestHTTPEndpoint(GroupResource.class)
public class GroupResourceTest {

    @Inject
    GroupRepository repository;

    @Inject
    GroupService service;

    @BeforeEach
    public void setup() {
        repository.deleteAll();
    }

    @Test
    public void testGetAllEndpoint() {
        service.create("test");
        given()
                .when()
                .get()
                .then()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    public void testGetEndpoint() {
        Group group = service.create("test");
        given()
                .when()
                .get("/" + group.getId())
                .then()
                .statusCode(200)
                .body("id", is(group.getId()));
    }

    @Test
    public void testCreateEndpoint() {
        Group group = new Group();
        group.setName("test");
        given().body(group).contentType(ContentType.JSON)
                .when()
                .post()
                .then()
                .statusCode(200)
                .body("name", is(group.getName()));
    }

    @Test
    public void testUpdateEndpoint() {
        Group group = service.create("test");
        group.setPermissions(new HashSet<>(Arrays.asList("test.permission")));
        given().body(group).contentType(ContentType.JSON)
                .when()
                .patch("/" + group.getId())
                .then()
                .statusCode(200)
                .body("id", is(group.getId()));
    }

    @Test
    public void testDeleteEndpoint() {
        Group group = service.create("test");
        given().body(group).contentType(ContentType.JSON)
                .when()
                .delete("/" + group.getId())
                .then()
                .statusCode(200)
                .body("id", is(group.getId()));
    }
}
