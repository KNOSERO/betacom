package com.ravcube.betacom.api.integration;

import org.junit.jupiter.api.Test;
import java.util.Map;
import java.util.UUID;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;

@ApiIntegrationTest
class ShareItemEndpointTest extends ApiIntegrationTestBase {

    @Test
    void givenOwnerAndNewRecipient_whenSharingAsViewer_thenReturnsCreatedPermission() {
        AuthenticatedUser owner = authenticatedUser();
        User recipient = registerUser();
        Item item = createItem(owner.token());

        given()
                .auth().oauth2(owner.token())
                .contentType(JSON)
                .body(Map.of("userId", recipient.id(), "role", "VIEWER"))
        .when()
                .post("/items/{id}/share", item.id())
        .then()
                .statusCode(201)
                .body("itemId", equalTo(item.id().toString()))
                .body("userId", equalTo(recipient.id().toString()))
                .body("role", equalTo("VIEWER"));
    }

    @Test
    void givenExistingPermission_whenOwnerSharesAgain_thenReturnsOkAndUpdatesRole() {
        AuthenticatedUser owner = authenticatedUser();
        User recipient = registerUser();
        Item item = createItem(owner.token());
        share(owner.token(), item.id(), recipient.id(), "VIEWER");

        given()
                .auth().oauth2(owner.token())
                .contentType(JSON)
                .body(Map.of("userId", recipient.id(), "role", "EDITOR"))
        .when()
                .post("/items/{id}/share", item.id())
        .then()
                .statusCode(200)
                .body("role", equalTo("EDITOR"));
    }

    @Test
    void givenInvalidPermissionRequest_whenSharing_thenReturnsBadRequest() {
        AuthenticatedUser owner = authenticatedUser();
        Item item = createItem(owner.token());

        given()
                .auth().oauth2(owner.token())
                .contentType(JSON)
                .body(Map.of("userId", UUID.randomUUID(), "role", "ADMIN"))
        .when()
                .post("/items/{id}/share", item.id())
        .then()
                .statusCode(400);
    }

    @Test
    void givenOwner_whenSharingItemWithSelf_thenReturnsBadRequest() {
        AuthenticatedUser owner = authenticatedUser();
        Item item = createItem(owner.token());

        given()
                .auth().oauth2(owner.token())
                .contentType(JSON)
                .body(Map.of("userId", owner.user().id(), "role", "VIEWER"))
        .when()
                .post("/items/{id}/share", item.id())
        .then()
                .statusCode(400)
                .body("title", equalTo("Invalid permission"));
    }

    @Test
    void givenAnonymousRequest_whenSharingItem_thenReturnsUnauthorized() {
        given()
                .contentType(JSON)
                .body(Map.of("userId", UUID.randomUUID(), "role", "VIEWER"))
        .when()
                .post("/items/{id}/share", UUID.randomUUID())
        .then()
                .statusCode(401);
    }

    @Test
    void givenNonOwner_whenSharingItem_thenReturnsForbidden() {
        AuthenticatedUser owner = authenticatedUser();
        AuthenticatedUser otherUser = authenticatedUser();
        User recipient = registerUser();
        Item item = createItem(owner.token());

        given()
                .auth().oauth2(otherUser.token())
                .contentType(JSON)
                .body(Map.of("userId", recipient.id(), "role", "VIEWER"))
        .when()
                .post("/items/{id}/share", item.id())
        .then()
                .statusCode(403);
    }

    @Test
    void givenMissingItem_whenSharing_thenReturnsNotFound() {
        AuthenticatedUser owner = authenticatedUser();
        User recipient = registerUser();

        given()
                .auth().oauth2(owner.token())
                .contentType(JSON)
                .body(Map.of("userId", recipient.id(), "role", "VIEWER"))
        .when()
                .post("/items/{id}/share", UUID.randomUUID())
        .then()
                .statusCode(404);
    }

    @Test
    void givenMissingRecipient_whenSharing_thenReturnsNotFound() {
        AuthenticatedUser owner = authenticatedUser();
        Item item = createItem(owner.token());

        given()
                .auth().oauth2(owner.token())
                .contentType(JSON)
                .body(Map.of("userId", UUID.randomUUID(), "role", "VIEWER"))
        .when()
                .post("/items/{id}/share", item.id())
        .then()
                .statusCode(404);
    }

    private void share(String token, UUID itemId, UUID userId, String role) {
        given()
                .auth().oauth2(token)
                .contentType(JSON)
                .body(Map.of("userId", userId, "role", role))
        .when()
                .post("/items/{id}/share", itemId)
        .then()
                .statusCode(201);
    }
}
