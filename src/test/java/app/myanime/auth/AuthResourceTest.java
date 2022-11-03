package app.myanime.auth;

import app.myanime.auth.model.User;
import app.myanime.auth.model.Verification;
import app.myanime.auth.resource.AuthResource;
import app.myanime.auth.resource.request.AuthLoginBasicRequest;
import app.myanime.auth.resource.request.AuthLoginTokenRequest;
import app.myanime.auth.resource.request.AuthRegisterRequest;
import app.myanime.auth.resource.request.AuthVerifyRequest;
import app.myanime.auth.service.UserService;
import app.myanime.auth.service.VerificationService;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@QuarkusTest
@TestHTTPEndpoint(AuthResource.class)
public class AuthResourceTest {

    @Inject
    UserService userService;


    @Inject
    VerificationService verificationService;

    @Test
    public void testLoginBasicEndpoint() {
        userService.create("testbasic", "1234", "test@test");
        given().body(new AuthLoginBasicRequest("testbasic", "1234")).contentType(ContentType.JSON)
                .when()
                .post("/login/basic")
                .then()
                .statusCode(200)
                .body("user.id", is("testbasic"));
    }

    @Test
    public void testLoginTokenEndpoint() {
        User user = userService.create("testtoken", "1234", "test@test");
        String token = userService.generateToken(user);
        given().body(new AuthLoginTokenRequest(token)).contentType(ContentType.JSON)
                .when()
                .post("/login/token")
                .then()
                .statusCode(200)
                .body("user.id", is("testtoken"));
    }

    @Test
    public void testRegisterEndpoint() {
        given().body(new AuthRegisterRequest("testregister", "testregister@test", "1234")).contentType(ContentType.JSON)
                .when()
                .post("/register")
                .then()
                .statusCode(200)
                .body("user.id", is("testregister"));
    }

    @Test
    public void testVerifyEndpoint() {
        User user = userService.create("testverify", "1234", "testverify@test");
        Verification verification = verificationService.send(user.getId(), user.getMail());
        given().body(new AuthVerifyRequest(verification.getUser(), verification.getId())).contentType(ContentType.JSON)
                .when()
                .post("/verify")
                .then()
                .statusCode(200)
                .body("user.groups", is(Matchers.contains(userService.getGroupService().getDefaultVerifyGroup().getId())));
    }
}
