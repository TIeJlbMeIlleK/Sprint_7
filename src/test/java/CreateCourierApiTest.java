import constants.Api;
import constants.ContentType;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;


public class CreateCourierApiTest {

    private static final File createCourier = new File("src/test/resources/createCourier.json");
    private static final File loginCourier = new File("src/test/resources/loginCourier.json");
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
    public void createCourier() {
        given()
                .contentType(ContentType.CONTENT_TYPE)
                .body(createCourier)
                .when()
                .post(Api.COURIER_API_ENDPOINT)
                .then()
                .statusCode(201)
                .body("ok", equalTo(true));
    }

    @Step("Попытка создания уже существующего клиента")
    public void createDuplicateCourier() {
        given()
                .contentType(ContentType.CONTENT_TYPE)
                .body(createCourier)
                .when()
                .post(Api.COURIER_API_ENDPOINT)
                .then()
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Step("Создание клиента с неполными данными в Json")
    public void createBadCourier() {
        given()
                .contentType(ContentType.CONTENT_TYPE)
                .body(badCourier)
                .when()
                .post(Api.COURIER_API_ENDPOINT)
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
    public static int loginAndExtractCourierId() {
        Response response = given()
                .contentType(ContentType.CONTENT_TYPE)
                .body(loginCourier)
                .when()
                .post(Api.COURIER_LOGIN_API_ENDPOINT)
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .extract()
                .response();

        return response.path("id");
    }

    @Step("Удалить курьера по полученному ID")
    public static void deleteCourierById(int courierId) {
        given()
                .pathParam("id", courierId)
                .when()
                .delete("/api/v1/courier/{id}")
                .then()
                .statusCode(200)
                .body("ok", equalTo(true));
    }
}