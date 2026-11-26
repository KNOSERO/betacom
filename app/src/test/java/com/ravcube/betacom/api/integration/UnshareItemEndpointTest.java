package com.ravcube.betacom.api.integration;

import org.junit.jupiter.api.Test;
import java.util.Map;
import java.util.UUID;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

@ApiIntegrationTest
class UnshareItemEndpointTest extends ApiIntegrationTestBase {

    @Test
    void givenOwnerAndExistingPermission_whenRevokingShare_thenReturnsNoContent() {
        AuthenticatedUser owner = authenticatedUser();
        User recipient = registerUser();
        Item item = createItem(owner.token());
        createShare(owner.token(), item.id(), recipient.id());

        given()
                .auth().oauth2(owner.token())
        .when()
                .delete("/items/{id}/share/{userId}", item.id(), recipient.id())
        .then()
                .statusCode(204);
    }

    @Test
    void givenAnonymousRequest_whenRevokingShare_thenReturnsUnauthorized() {
        given()
        .when()
                .delete("/items/{id}/share/{userId}", UUID.randomUUID(), UUID.randomUUID())
        .then()
                .statusCode(401);
    }

    @Test
    void givenNonOwner_whenRevokingShare_thenReturnsForbidden() {
        AuthenticatedUser owner = authenticatedUser();
        AuthenticatedUser otherUser = authenticatedUser();
        User recipient = registerUser();
        Item item = createItem(owner.token());
        createShare(owner.token(), item.id(), recipient.id());

        given()
                .auth().oauth2(otherUser.token())
        .when()
                .delete("/items/{id}/share/{userId}", item.id(), recipient.id())
        .then()
                .statusCode(403);
    }

    @Test
    void givenMissingPermission_whenRevokingShare_thenReturnsNotFound() {
        AuthenticatedUser owner = authenticatedUser();
        Item item = createItem(owner.token());

        given()
                .auth().oauth2(owner.token())
        .when()
                .delete("/items/{id}/share/{userId}", item.id(), UUID.randomUUID())
        .then()
                .statusCode(404);
    }

    private void createShare(String token, UUID itemId, UUID userId) {
        given()
                .auth().oauth2(token)
                .contentType(JSON)
                .body(Map.of("userId", userId, "role", "VIEWER"))
        .when()
                .post("/items/{id}/share", itemId)
        .then()
                .statusCode(201);
    }
}
