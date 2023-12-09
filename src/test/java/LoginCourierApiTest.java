import api.client.CourierClient;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LoginCourierApiTest {
    CourierClient courierClient = new CourierClient();

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

    @Step("Создание курьера для выполнения теста")
    private void createCourier() {
        courierClient.createCourier();
    }

    @Step("Авторизация курьера")
    private void loginCourier() {
        courierClient.loginCourier();
    }

    @Step("Попытка авторизации с не корректными данными")
    private void badLoginCourier() {
        courierClient.badLoginCourier();
    }

    @Step("Авторизация без обязательных полей")
    private void notFullLoginCourier() {
        courierClient.notFullLoginCourier();
    }

    @After
    public void deleteCourier() {
        int courierId = loginAndExtractCourierId();
        deleteCourierById(courierId);
    }

    @Step("Авторизоваться за созданного клиента, для получения ID")
    public static int loginAndExtractCourierId() {
        return CourierClient.loginAndExtractCourierId();
    }

    @Step("Удалить курьера по полученному ID")
    public static void deleteCourierById(int courierId) {
        CourierClient.deleteCourierById(courierId);
    }

}
