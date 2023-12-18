import constants.Api;
import constants.ContentType;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@RunWith(Parameterized.class)
public class OrdersApiTest {
    private final Order order;

    @Before
    public void beforeTest() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    public OrdersApiTest(Order order) {
        this.order = order;
    }

    @Parameterized.Parameters
    public static Object[][] getData() {
        return new Object[][] {
                {new Order("Naruto","Uchiha","Konoha, 142 apt.",4,"+7 800 355 35 35",5,"2020-06-06","Saske, come back to Konoha", List.of("BLACK"))},
                {new Order("Naruto","Uchiha","Konoha, 142 apt.",4,"+7 800 355 35 35",5,"2020-06-06","Saske, come back to Konoha", List.of("GREY"))},
                {new Order("Naruto","Uchiha","Konoha, 142 apt.",4,"+7 800 355 35 35",5,"2020-06-06","Saske, come back to Konoha", List.of("BLACK","GREY"))},
                {new Order("Naruto","Uchiha","Konoha, 142 apt.",4,"+7 800 355 35 35",5,"2020-06-06","Saske, come back to Konoha", List.of())},
        };
    }

    @Test
    @Step("Сделать заказ")
    public void makeOrder() {
        given()
                .contentType(ContentType.CONTENT_TYPE)
                .body(order)
                .when()
                .post(Api.MAKE_ORDER_ENDPOINT)
                .then()
                .statusCode(201)
                .body("track", notNullValue());
    }
}
