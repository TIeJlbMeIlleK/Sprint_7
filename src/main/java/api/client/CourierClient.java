package api.client;

import constants.Api;
import constants.ContentType;
import io.restassured.response.Response;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CourierClient {
    private static final File createCourier = new File("src/test/resources/createCourier.json");
    private static final File loginCourier = new File("src/test/resources/loginCourier.json");
    private static final File badCourier = new File("src/test/resources/badCourier.json");
    private static final File badPassword = new File("src/test/resources/badPassword.json");


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

    public void loginCourier() {
        given()
                .contentType(ContentType.CONTENT_TYPE)
                .body(loginCourier)
                .when()
                .post(Api.COURIER_LOGIN_API_ENDPOINT)
                .then()
                .statusCode(200)
                .body("id", notNullValue());
    }

    public void badLoginCourier() {
        given()
                .contentType(ContentType.CONTENT_TYPE)
                .body(badPassword)
                .when()
                .post(Api.COURIER_LOGIN_API_ENDPOINT)
                .then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    public void notFullLoginCourier() {
        given()
                .contentType(ContentType.CONTENT_TYPE)
                .body(badCourier)
                .when()
                .post(Api.COURIER_LOGIN_API_ENDPOINT)
                .then()
                .statusCode(504);
    }

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
