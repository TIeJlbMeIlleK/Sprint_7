import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


public class CreateCourierApiTest {
    private final String COURIER_API_ENDPOINT = "/api/v1/courier";
    private static final String CONTENT_TYPE = "application/json";
    private static final Courier createCourier = new Courier("ve_ITDGroup", "ve_ITDGroup", "ve_ITDGroup");
    private static final Courier deleteCourier = new Courier("ve_ITDGroup", "ve_ITDGroup");
    private static final File badCourier = new File("src/test/resources/badCourier.json");

    @Before
    public void beforeTest() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    @DisplayName("Тест на проверку API по работе с Курьерами") // имя теста
    @Description("Проверка работы API по созданию курьеров, созданию дублей курьеров, созданию курьеров по невалидным данным")
    @Test
    public void testCourierApi() {
        createCourier();
        createDuplicateCourier();
        createBadCourier();
    }

    @Step("Создание клиента")
    private void createCourier() {
        given()
                .contentType(CONTENT_TYPE)
                .body(createCourier)
                .when()
                .post(COURIER_API_ENDPOINT)
                .then()
                .statusCode(201)
                .body("ok", equalTo(true));
    }

    @Step("Попытка создания уже существующего клиента")
    private void createDuplicateCourier() {
        given()
                .contentType(CONTENT_TYPE)
                .body(createCourier)
                .when()
                .post(COURIER_API_ENDPOINT)
                .then()
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Step("Создание клиента с неполными данными в Json")
    private void createBadCourier() {
        given()
                .contentType(CONTENT_TYPE)
                .body(badCourier)
                .when()
                .post(COURIER_API_ENDPOINT)
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @AfterClass
    public static void deleteCourier() {
        int courierId = loginAndExtractCourierId();
        deleteCourierById(courierId);
    }

    @Step("Авторизоваться за созданного клиента, для получения ID")
    private static int loginAndExtractCourierId() {
        Response response = given()
                .contentType(CONTENT_TYPE)
                .body(deleteCourier)
                .when()
                .post("/api/v1/courier/login")
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .extract()
                .response();

        return response.path("id");
    }

    @Step("Удалить курьера по полученному ID")
    private static void deleteCourierById(int courierId) {
        given()
                .pathParam("id", courierId)
                .when()
                .delete("/api/v1/courier/{id}")
                .then()
                .statusCode(200)
                .body("ok", equalTo(true));
    }
}

