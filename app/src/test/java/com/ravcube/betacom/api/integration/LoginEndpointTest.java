package com.ravcube.betacom.api.integration;

import org.junit.jupiter.api.Test;
import java.util.Map;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@ApiIntegrationTest
class LoginEndpointTest extends ApiIntegrationTestBase {

    @Test
    void givenValidCredentials_whenLoggingIn_thenReturnsJwt() {
        User user = registerUser();

        given()
                .contentType(JSON)
                .body(Map.of("login", user.login(), "password", user.password()))
        .when()
                .post("/login")
        .then()
                .statusCode(200)
                .body("token", notNullValue())
                .body("expiresIn", notNullValue());
    }

    @Test
    void givenInvalidLoginRequest_whenLoggingIn_thenReturnsBadRequest() {
        given()
                .contentType(JSON)
                .body(Map.of("login", "missing-password"))
        .when()
                .post("/login")
        .then()
                .statusCode(400)
                .body("status", equalTo(400));
    }

    @Test
    void givenIncorrectPassword_whenLoggingIn_thenReturnsUnauthorized() {
        User user = registerUser();

        given()
                .contentType(JSON)
                .body(Map.of("login", user.login(), "password", "wrong-password"))
        .when()
                .post("/login")
        .then()
                .statusCode(401)
                .body("title", equalTo("Unauthorized"));
    }
}
