package com.ravcube.betacom.api.integration;

import org.junit.jupiter.api.Test;
import java.util.Map;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;

@ApiIntegrationTest
class CreateItemEndpointTest extends ApiIntegrationTestBase {

    @Test
    void givenAuthenticatedOwner_whenPostingValidItem_thenReturnsCreatedItem() {
        AuthenticatedUser owner = authenticatedUser();

        given()
                .auth().oauth2(owner.token())
                .contentType(JSON)
                .body(Map.of("title", "New item", "content", "Item content"))
        .when()
                .post("/items")
        .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("title", equalTo("New item"))
                .body("content", equalTo("Item content"))
                .body("ownerId", equalTo(owner.user().id().toString()));
    }

    @Test
    void givenAnonymousRequest_whenPostingItem_thenReturnsUnauthorized() {
        given()
                .contentType(JSON)
                .body(Map.of("title", "New item", "content", "Item content"))
        .when()
                .post("/items")
        .then()
                .statusCode(401);
    }

    @Test
    void givenBlankTitle_whenPostingItem_thenReturnsBadRequest() {
        AuthenticatedUser owner = authenticatedUser();

        given()
                .auth().oauth2(owner.token())
                .contentType(JSON)
                .body(Map.of("title", " ", "content", "Item content"))
        .when()
                .post("/items")
        .then()
                .statusCode(400)
                .body("status", equalTo(400));
    }
}
