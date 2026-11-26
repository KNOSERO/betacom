package com.ravcube.betacom.api.integration;

import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@ApiIntegrationTest
class ListItemsEndpointTest extends ApiIntegrationTestBase {

    @Test
    void givenAnonymousRequest_whenListingItems_thenReturnsUnauthorized() {
        given()
        .when()
                .get("/items")
        .then()
                .statusCode(401);
    }

    @Test
    void givenOwnerWithItems_whenListingItems_thenOnlyVisibleItemsAreReturned() {
        AuthenticatedUser owner = authenticatedUser();
        Item ownedItem = createItem(owner.token());
        AuthenticatedUser otherUser = authenticatedUser();
        Item privateItem = createItem(otherUser.token());

        given()
                .auth().oauth2(owner.token())
        .when()
                .get("/items")
        .then()
                .statusCode(200)
                .body("id", hasItem(ownedItem.id().toString()))
                .body("id", not(hasItem(privateItem.id().toString())))
                .body("myRole", hasItem("OWNER"));
    }
}
