package com.ravcube.betacom.api.integration;

import org.junit.jupiter.api.Test;
import java.util.Map;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;

@ApiIntegrationTest
class RegisterEndpointTest extends ApiIntegrationTestBase {

    @Test
    void givenValidRegistration_whenPostingRegister_thenReturnsCreatedUser() {
        given()
                .contentType(JSON)
                .body(Map.of("login", "it-register-" + java.util.UUID.randomUUID(), "password", "safe-password"))
        .when()
                .post("/register")
        .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("login", startsWith("it-register-"))
                .body("createdAt", notNullValue());
    }

    @Test
    void givenInvalidRegistration_whenPostingRegister_thenReturnsBadRequest() {
        given()
                .contentType(JSON)
                .body(Map.of("login", "x", "password", "short"))
        .when()
                .post("/register")
        .then()
                .statusCode(400)
                .body("status", equalTo(400));
    }

    @Test
    void givenExistingLogin_whenPostingRegisterAgain_thenReturnsConflict() {
        User existingUser = registerUser();

        given()
                .contentType(JSON)
                .body(Map.of("login", existingUser.login(), "password", existingUser.password()))
        .when()
                .post("/register")
        .then()
                .statusCode(409)
                .body("title", equalTo("Login already taken"));
    }
}
