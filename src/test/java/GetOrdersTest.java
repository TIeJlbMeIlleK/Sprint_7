import io.qameta.allure.Step;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class GetOrdersTest {
    @Before
    public void beforeTest() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    @Test
    @Step("Получение списка заказов")
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
