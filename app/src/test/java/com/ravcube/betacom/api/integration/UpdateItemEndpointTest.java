package com.ravcube.betacom.api.integration;

import org.junit.jupiter.api.Test;
import java.util.Map;
import java.util.UUID;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.equalTo;

@ApiIntegrationTest
class UpdateItemEndpointTest extends ApiIntegrationTestBase {

    @Test
    void givenOwnerAndCurrentVersion_whenPatchingItem_thenReturnsUpdatedItem() {
        AuthenticatedUser owner = authenticatedUser();
        Item item = createItem(owner.token());

        given()
                .auth().oauth2(owner.token())
                .contentType(JSON)
                .body(Map.of("title", "Updated title", "content", "Updated body", "version", item.version()))
        .when()
                .patch("/items/{id}", item.id())
        .then()
                .statusCode(200)
                .body("id", equalTo(item.id().toString()))
                .body("title", equalTo("Updated title"))
                .body("content", equalTo("Updated body"));
    }

    @Test
    void givenInvalidVersionField_whenPatchingItem_thenReturnsBadRequest() {
        AuthenticatedUser owner = authenticatedUser();
        Item item = createItem(owner.token());

        given()
                .auth().oauth2(owner.token())
                .contentType(JSON)
                .body(Map.of("title", "Updated title", "version", -1))
        .when()
                .patch("/items/{id}", item.id())
        .then()
                .statusCode(400);
    }

    @Test
    void givenAnonymousRequest_whenPatchingItem_thenReturnsUnauthorized() {
        given()
                .contentType(JSON)
                .body(Map.of("title", "Updated title", "version", 0))
        .when()
                .patch("/items/{id}", UUID.randomUUID())
        .then()
                .statusCode(401);
    }

    @Test
    void givenNonOwner_whenPatchingItem_thenReturnsForbidden() {
        AuthenticatedUser owner = authenticatedUser();
        AuthenticatedUser otherUser = authenticatedUser();
        Item item = createItem(owner.token());

        given()
                .auth().oauth2(otherUser.token())
                .contentType(JSON)
                .body(Map.of("title", "Updated title", "version", item.version()))
        .when()
                .patch("/items/{id}", item.id())
        .then()
                .statusCode(403);
    }

    @Test
    void givenMissingItem_whenPatching_thenReturnsNotFound() {
        AuthenticatedUser owner = authenticatedUser();

        given()
                .auth().oauth2(owner.token())
                .contentType(JSON)
                .body(Map.of("title", "Updated title", "version", 0))
        .when()
                .patch("/items/{id}", UUID.randomUUID())
        .then()
                .statusCode(404);
    }

    @Test
    void givenStaleVersion_whenPatchingItem_thenReturnsConflict() {
        AuthenticatedUser owner = authenticatedUser();
        Item item = createItem(owner.token());

        given()
                .auth().oauth2(owner.token())
                .contentType(JSON)
                .body(Map.of("title", "Updated title", "version", item.version() + 10))
        .when()
                .patch("/items/{id}", item.id())
        .then()
                .statusCode(409)
                .body("title", equalTo("Version conflict"))
                .body("currentVersion", equalTo(item.version()));
    }
}
