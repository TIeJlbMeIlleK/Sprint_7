import constants.Api;
import constants.ContentType;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class LoginCourierApiTest {
    private final Courier createCourier = new Courier("ve_ITDGroup", "ve_ITDGroup", "ve_ITDGroup");
    private final Courier loginCourier = new Courier("ve_ITDGroup", "ve_ITDGroup");
    private final Courier notFullLoginCourier = new Courier("ve_ITDGroup");
    private final Courier badLoginCourier = new Courier("ve_ITDGroup", "ve_ITDGroup55");

    @Before
    public void beforeTest() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
        createCourier();
    }

    @Test
    public void testCourierApi() {
        loginCourier();
        badLoginCourier();
        notFullLoginCourier();
    }

    @After
    public void deleteCourier() {
        int courierId = loginAndExtractCourierId();
        deleteCourierById(courierId);
    }

    @Step("Создание курьера для выполнения теста")
    private void createCourier() {
        given()
                .contentType(ContentType.CONTENT_TYPE)
                .body(createCourier)
                .when()
                .post("/api/v1/courier")
                .then()
                .statusCode(201)
                .body("ok", equalTo(true));
    }

    @Step("Авторизация курьера")
    private void loginCourier() {
        given()
                .contentType(ContentType.CONTENT_TYPE)
                .body(loginCourier)
                .when()
                .post(Api.COURIER_LOGIN_API_ENDPOINT)
                .then()
                .statusCode(200)
                .body("id", notNullValue());
    }

    @Step("Попытка авторизации с не корректными данными")
    private void badLoginCourier() {
        given()
                .contentType(ContentType.CONTENT_TYPE)
                .body(badLoginCourier)
                .when()
                .post(Api.COURIER_LOGIN_API_ENDPOINT)
                .then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Step("Авторизация без обязательных полей")
    private void notFullLoginCourier() {
        given()
                .contentType(ContentType.CONTENT_TYPE)
                .body(notFullLoginCourier)
                .when()
                .post(Api.COURIER_LOGIN_API_ENDPOINT)
                .then()
                .statusCode(504);
    }

    @Step("Авторизация для получения ID по курьеру")
    private int loginAndExtractCourierId() {
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

    @Step("Удаление курьера по ID")
    private void deleteCourierById(int courierId) {
        given()
                .pathParam("id", courierId)
                .when()
                .delete("/api/v1/courier/{id}")
                .then()
                .statusCode(200)
                .body("ok", equalTo(true));
    }
}
