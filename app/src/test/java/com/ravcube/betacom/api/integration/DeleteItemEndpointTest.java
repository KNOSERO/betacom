package com.ravcube.betacom.api.integration;

import org.junit.jupiter.api.Test;
import java.util.UUID;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@ApiIntegrationTest
class DeleteItemEndpointTest extends ApiIntegrationTestBase {

    @Test
    void givenOwner_whenDeletingItem_thenReturnsNoContentAndItemIsGone() {
        AuthenticatedUser owner = authenticatedUser();
        Item item = createItem(owner.token());

        given()
                .auth().oauth2(owner.token())
        .when()
                .delete("/items/{id}", item.id())
        .then()
                .statusCode(204);

        given()
                .auth().oauth2(owner.token())
        .when()
                .get("/items")
        .then()
                .statusCode(200)
                .body("id", not(hasItem(item.id().toString())));
    }

    @Test
    void givenAnonymousRequest_whenDeletingItem_thenReturnsUnauthorized() {
        given()
        .when()
                .delete("/items/{id}", UUID.randomUUID())
        .then()
                .statusCode(401);
    }

    @Test
    void givenNonOwner_whenDeletingItem_thenReturnsForbidden() {
        AuthenticatedUser owner = authenticatedUser();
        AuthenticatedUser otherUser = authenticatedUser();
        Item item = createItem(owner.token());

        given()
                .auth().oauth2(otherUser.token())
        .when()
                .delete("/items/{id}", item.id())
        .then()
                .statusCode(403);
    }

    @Test
    void givenMissingItem_whenDeleting_thenReturnsNotFound() {
        AuthenticatedUser owner = authenticatedUser();

        given()
                .auth().oauth2(owner.token())
        .when()
                .delete("/items/{id}", UUID.randomUUID())
        .then()
                .statusCode(404);
    }
}
