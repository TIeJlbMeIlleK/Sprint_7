import api.client.CourierClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class CreateCourierTest {
    CourierClient courierClient = new CourierClient();

    @Before
    public void beforeTest() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    @DisplayName("Тест на проверку API по работе с Курьерами") // имя теста
    @Description("Проверка работы API по созданию курьеров, созданию дублей курьеров, созданию курьеров по невалидным данным")
    @Test
    public void testCourierApi() {
        courierClient.createCourier();
    }

    @After
    public void deleteCourier() {
        int courierId = CourierClient.loginAndExtractCourierId();
        CourierClient.deleteCourierById(courierId);
    }
}