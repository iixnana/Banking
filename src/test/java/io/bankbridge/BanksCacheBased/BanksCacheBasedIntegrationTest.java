package io.bankbridge.BanksCacheBased;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;
import static spark.Spark.awaitInitialization;
import static spark.Spark.stop;

import io.bankbridge.Main;
import io.bankbridge.TestUtil.Http.HttpClient;
import io.bankbridge.TestUtil.Http.HttpResponse;
import io.bankbridge.TestUtil.SparkManager.BankbridgeSparkManager;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class BanksCacheBasedIntegrationTest {
    private final static String host = "localhost";
    private final static String heartbeatRoute = "/heartbeat";
    private static int port;
    private static HttpClient client;

    @BeforeClass
    public static void setUp() {
        BankbridgeSparkManager.get(host, heartbeatRoute).startSparkAppIfNotRunning(8080);
        port = BankbridgeSparkManager.get(host, heartbeatRoute).getPort();
        client = new HttpClient(host, port);
    }

    @AfterClass
    public static void tearDown() {
        BankbridgeSparkManager.get(host, heartbeatRoute).killServer();
    }

    @Test
    public void getAllBanksTest() {
        String route = "/v1/banks/all";
        HttpResponse response = client.request("GET", route);

        assertEquals(200, response.getStatus());
        List<Map<String, String>> result = (List<Map<String, String>>) response.getJson();
        assertNotNull(result);
        for (Map<String, String> element : result) {
            assertNotNull(element.get("id"));
            assertNotNull(element.get("name"));
        }
    }
}
