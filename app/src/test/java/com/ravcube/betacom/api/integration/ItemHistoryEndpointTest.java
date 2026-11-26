package com.ravcube.betacom.api.integration;

import org.junit.jupiter.api.Test;
import io.restassured.response.Response;
import java.util.List;
import java.util.UUID;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ApiIntegrationTest
class ItemHistoryEndpointTest extends ApiIntegrationTestBase {

    @Test
    void givenOwner_whenRequestingHistory_thenReturnsItemRevisions() {
        AuthenticatedUser owner = authenticatedUser();
        Item item = createItem(owner.token());

        for (int edit = 1; edit <= 3; edit++) {
            Response updateResponse = given()
                    .auth().oauth2(owner.token())
                    .contentType(JSON)
                    .body(java.util.Map.of(
                            "title", "Edit " + edit,
                            "content", "Content after edit " + edit,
                            "version", item.version()))
            .when()
                    .patch("/items/{id}", item.id())
            .then()
                    .statusCode(200)
                    .extract()
                    .response();
            item = new Item(item.id(), updateResponse.path("version"));
        }

        Response history = given()
                .auth().oauth2(owner.token())
        .when()
                .get("/items/{id}/history", item.id())
        .then()
                .statusCode(200)
                .extract()
                .response();

        List<Integer> revisions = history.jsonPath().getList("revision", Integer.class);
        assertEquals(4, revisions.size(), "history must contain creation and all three edits");
        for (int index = 1; index < revisions.size(); index++) {
            assertTrue(revisions.get(index) > revisions.get(index - 1), "revisions must be returned in ascending order");
        }
        assertEquals(List.of("ADD", "MOD", "MOD", "MOD"),
                history.jsonPath().getList("revisionType"));
        assertEquals(List.of("Integration item", "Edit 1", "Edit 2", "Edit 3"),
                history.jsonPath().getList("title"));
        assertEquals(List.of(owner.user().id().toString(), owner.user().id().toString(),
                        owner.user().id().toString(), owner.user().id().toString()),
                history.jsonPath().getList("createdBy"));
    }

    @Test
    void givenAnonymousRequest_whenRequestingHistory_thenReturnsUnauthorized() {
        given()
        .when()
                .get("/items/{id}/history", UUID.randomUUID())
        .then()
                .statusCode(401);
    }

    @Test
    void givenUserWithoutReadPermission_whenRequestingHistory_thenReturnsForbidden() {
        AuthenticatedUser owner = authenticatedUser();
        AuthenticatedUser otherUser = authenticatedUser();
        Item item = createItem(owner.token());

        given()
                .auth().oauth2(otherUser.token())
        .when()
                .get("/items/{id}/history", item.id())
        .then()
                .statusCode(403);
    }

    @Test
    void givenMissingItem_whenRequestingHistory_thenReturnsNotFound() {
        AuthenticatedUser owner = authenticatedUser();

        given()
                .auth().oauth2(owner.token())
        .when()
                .get("/items/{id}/history", UUID.randomUUID())
        .then()
                .statusCode(404);
    }
}
