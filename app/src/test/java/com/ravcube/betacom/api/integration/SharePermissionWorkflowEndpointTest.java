package com.ravcube.betacom.api.integration;

import org.junit.jupiter.api.Test;

import java.util.Map;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ApiIntegrationTest
class SharePermissionWorkflowEndpointTest extends ApiIntegrationTestBase {

    @Test
    void givenSharedItem_whenEditorAndViewerUseAndLoseAccess_thenRolesAreEnforced() {
        AuthenticatedUser owner = authenticatedUser();
        User editor = registerUser();
        User viewer = registerUser();
        User anotherUser = registerUser();
        Item item = createItem(owner.token());

        // Owner grants EDITOR and VIEWER access.
        share(owner.token(), item, editor, "EDITOR", 201);
        share(owner.token(), item, viewer, "VIEWER", 201);

        String editorToken = tokenFor(editor);
        String viewerToken = tokenFor(viewer);

        // An editor can update the item.
        int updatedVersion = given()
                .auth().oauth2(editorToken)
                .contentType(JSON)
                .body(Map.of("title", "Edited by editor", "version", item.version()))
        .when()
                .patch("/items/{id}", item.id())
        .then()
                .statusCode(200)
                .body("title", equalTo("Edited by editor"))
                .extract()
                .path("version");

        // A viewer can see the item but cannot update it.
        given()
                .auth().oauth2(viewerToken)
        .when()
                .get("/items")
        .then()
                .statusCode(200)
                .body("id", hasItem(item.id().toString()))
                .body("myRole", hasItem("VIEWER"));

        given()
                .auth().oauth2(viewerToken)
                .contentType(JSON)
                .body(Map.of("title", "Viewer edit", "version", updatedVersion))
        .when()
                .patch("/items/{id}", item.id())
        .then()
                .statusCode(403);

        // An editor cannot grant permissions; only the owner can.
        given()
                .auth().oauth2(editorToken)
                .contentType(JSON)
                .body(Map.of("userId", anotherUser.id(), "role", "VIEWER"))
        .when()
                .post("/items/{id}/share", item.id())
        .then()
                .statusCode(403);

        // Revoking the viewer's grant removes both list visibility and history access.
        given()
                .auth().oauth2(owner.token())
        .when()
                .delete("/items/{id}/share/{userId}", item.id(), viewer.id())
        .then()
                .statusCode(204);

        given()
                .auth().oauth2(viewerToken)
        .when()
                .get("/items")
        .then()
                .statusCode(200)
                .body("id", not(hasItem(item.id().toString())));

        given()
                .auth().oauth2(viewerToken)
        .when()
                .get("/items/{id}/history", item.id())
        .then()
                .statusCode(403);

        assertEquals(1, updatedVersion);
    }

    private void share(String token, Item item, User recipient, String role, int expectedStatus) {
        given()
                .auth().oauth2(token)
                .contentType(JSON)
                .body(Map.of("userId", recipient.id(), "role", role))
        .when()
                .post("/items/{id}/share", item.id())
        .then()
                .statusCode(expectedStatus);
    }
}
