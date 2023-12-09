package api.client;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.empty;

public class OrdersClient {

    public void testGetOrders() {
        given()
                .when()
                .get("/api/v1/orders")
                .then()
                .statusCode(200)
                .body("orders", not(empty()))
                .body("pageInfo.page", equalTo(0))
                .body("pageInfo.total", greaterThan(0))
                .body("pageInfo.limit", greaterThan(0))
                .body("availableStations", not(empty()));
    }
}
