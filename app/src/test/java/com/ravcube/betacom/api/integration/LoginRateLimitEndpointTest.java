package com.ravcube.betacom.api.integration;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestPropertySource;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ApiIntegrationTest
@TestPropertySource(properties = {
        "security.rate-limit.login.capacity=5",
        "security.rate-limit.login.window=PT1M"
})
class LoginRateLimitEndpointTest extends ApiIntegrationTestBase {

    @Test
    void givenSixLoginRequestsFromSameClient_whenRateLimitIsFive_thenSixthReturns429WithRetryAfter() {
        Map<String, String> invalidCredentials = Map.of(
                "login", "unknown-user",
                "password", "wrong-password");

        // The first five requests from this test client are allowed through to authentication.
        for (int requestNumber = 1; requestNumber <= 5; requestNumber++) {
            given()
                    .contentType(JSON)
                    .body(invalidCredentials)
            .when()
                    .post("/login")
            .then()
                    .statusCode(401);
        }

        // The sixth request from the same client IP is blocked by the rate limiter.
        Response response = given()
                .contentType(JSON)
                .body(invalidCredentials)
        .when()
                .post("/login")
        .then()
                .statusCode(429)
                .header("Retry-After", org.hamcrest.Matchers.notNullValue())
                .extract()
                .response();

        long retryAfterSeconds = Long.parseLong(response.getHeader("Retry-After"));
        assertTrue(retryAfterSeconds > 0, "Retry-After must specify a positive number of seconds");
    }
}
