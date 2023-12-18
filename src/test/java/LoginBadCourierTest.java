import api.client.CourierClient;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LoginBadCourierTest {
    CourierClient courierClient = new CourierClient();

    @Before
    public void beforeTest() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    @Test
    public void testCourierApi() {
        courierClient.badLoginCourier();
    }
}
