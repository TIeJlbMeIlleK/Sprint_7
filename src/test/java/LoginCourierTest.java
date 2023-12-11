import api.client.CourierClient;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LoginCourierTest {
    CourierClient courierClient = new CourierClient();

    @Before
    public void beforeTest() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
        courierClient.createCourier();
    }

    @Test
    public void testCourierApi() {
        courierClient.loginCourier();
    }

    @After
    public void deleteCourier() {
        int courierId = CourierClient.loginAndExtractCourierId();
        CourierClient.deleteCourierById(courierId);
    }

}
