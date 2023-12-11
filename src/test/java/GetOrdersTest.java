import api.client.OrdersClient;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;

public class GetOrdersTest {
    OrdersClient ordersClient = new OrdersClient();

    @Before
    public void beforeTest() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    @Test
    public void testGetOrders() {
        ordersClient.testGetOrders();
    }
}
