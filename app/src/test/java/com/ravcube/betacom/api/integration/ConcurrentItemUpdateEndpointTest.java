package com.ravcube.betacom.api.integration;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ApiIntegrationTest
class ConcurrentItemUpdateEndpointTest extends ApiIntegrationTestBase {

    @Test
    void givenTwoUpdatesWithSameVersion_whenSentConcurrently_thenOnlyOneSucceeds() throws Exception {
        AuthenticatedUser owner = authenticatedUser();
        Item item = createItem(owner.token());
        ExecutorService requests = Executors.newFixedThreadPool(2);
        CountDownLatch ready = new CountDownLatch(2);
        CountDownLatch start = new CountDownLatch(1);

        try {
            Future<Integer> firstUpdate = requests.submit(() -> updateItem(
                    owner.token(), item, "Concurrent edit A", ready, start));
            Future<Integer> secondUpdate = requests.submit(() -> updateItem(
                    owner.token(), item, "Concurrent edit B", ready, start));

            assertTrue(ready.await(10, TimeUnit.SECONDS), "both HTTP requests should be ready before release");
            start.countDown();

            List<Integer> statuses = List.of(
                    firstUpdate.get(20, TimeUnit.SECONDS),
                    secondUpdate.get(20, TimeUnit.SECONDS));

            assertEquals(1, statuses.stream().filter(status -> status == 200).count(),
                    "one update should persist");
            assertEquals(1, statuses.stream().filter(status -> status == 409).count(),
                    "the competing update should detect the version conflict");
        } finally {
            start.countDown();
            requests.shutdownNow();
        }
    }

    private int updateItem(
            String token,
            Item item,
            String title,
            CountDownLatch ready,
            CountDownLatch start) throws InterruptedException {
        ready.countDown();
        if (!start.await(10, TimeUnit.SECONDS)) {
            throw new IllegalStateException("Concurrent update test did not release its requests");
        }

        Response response = given()
                .auth().oauth2(token)
                .contentType(JSON)
                .body(Map.of("title", title, "version", item.version()))
        .when()
                .patch("/items/{id}", item.id());
        return response.statusCode();
    }
}
