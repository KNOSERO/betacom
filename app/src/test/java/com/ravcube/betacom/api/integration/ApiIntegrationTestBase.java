package com.ravcube.betacom.api.integration;

import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.reset;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.notNullValue;

public abstract class ApiIntegrationTestBase {

    @LocalServerPort
    private int port;

    @BeforeEach
    void configureRestAssured() {
        io.restassured.RestAssured.baseURI = "http://localhost";
        io.restassured.RestAssured.port = port;
        io.restassured.RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @AfterEach
    void resetRestAssured() {
        reset();
    }

    protected User registerUser() {
        return registerUser("it-" + UUID.randomUUID());
    }

    protected User registerUser(String login) {
        String password = "integration-password";
        String id = given()
                .contentType(JSON)
                .body(Map.of("login", login, "password", password))
                .when()
                .post("/register")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .extract()
                .path("id");
        return new User(UUID.fromString(id), login, password);
    }

    protected String tokenFor(User user) {
        return given()
                .contentType(JSON)
                .body(Map.of("login", user.login(), "password", user.password()))
                .when()
                .post("/login")
                .then()
                .statusCode(200)
                .body("token", notNullValue())
                .extract()
                .path("token");
    }

    protected AuthenticatedUser authenticatedUser() {
        User user = registerUser();
        return new AuthenticatedUser(user, tokenFor(user));
    }

    protected Item createItem(String token) {
        Map<String, String> body = Map.of("title", "Integration item", "content", "Created through API");
        Response response = given()
                .auth().oauth2(token)
                .contentType(JSON)
                .body(body)
                .when()
                .post("/items")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .extract()
                .response();
        return new Item(UUID.fromString(response.path("id")), response.path("version"));
    }

    protected record User(UUID id, String login, String password) { }
    protected record AuthenticatedUser(User user, String token) { }
    protected record Item(UUID id, Integer version) { }
}
