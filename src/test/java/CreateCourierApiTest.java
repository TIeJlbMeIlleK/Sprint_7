import api.client.CourierClient;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class CreateCourierApiTest {
    CourierClient courierClient = new CourierClient();

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
        courierClient.createCourier();
    }

    @Step("Попытка создания уже существующего клиента")
    public void createDuplicateCourier() {
        courierClient.createDuplicateCourier();
    }

    @Step("Создание клиента с неполными данными в Json")
    public void createBadCourier() {
        courierClient.createBadCourier();
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