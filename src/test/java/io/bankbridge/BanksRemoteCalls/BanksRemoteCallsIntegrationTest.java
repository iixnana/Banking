package io.bankbridge.BanksRemoteCalls;

import io.bankbridge.TestUtil.Http.HttpClient;
import io.bankbridge.TestUtil.Http.HttpResponse;
import io.bankbridge.TestUtil.SparkManager.BankbridgeSparkManager;
import io.bankbridge.TestUtil.SparkManager.MockRemotesSparkManager;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BanksRemoteCallsIntegrationTest {
    private final static String host = "localhost";
    private final static String heartbeatRoute = "/heartbeat";
    private static int port;
    private static HttpClient client;

    @BeforeClass
    public static void setUp() {
        BankbridgeSparkManager.get(host, heartbeatRoute).startSparkAppIfNotRunning(8080);
        port = BankbridgeSparkManager.get(host, heartbeatRoute).getPort();
        client = new HttpClient(host, port);
        MockRemotesSparkManager.get(host, heartbeatRoute).startSparkAppIfNotRunning(1234);
    }

    @AfterClass
    public static void tearDown() {
        BankbridgeSparkManager.get(host, heartbeatRoute).killServer();
        MockRemotesSparkManager.get(host, heartbeatRoute).killServer();
    }

    @Test
    public void getAllBanksTest() {
        String route = "/v2/banks/all";
        HttpResponse response = client.request("GET", route);

        assertEquals(200, response.getStatus());
        List<Map<String, String>> result = (List<Map<String, String>>) response.getJson();
        //Expecting result to be not null
        assertNotNull(result);
        //Expecting more than zero elements
        assertTrue(result.size() > 0);
        //Check all keys and values for not null
        for (Map<String, String> element : result) {
            assertNotNull(element.get("id"));
            assertNotNull(element.get("name"));
        }
        //Check if result doesn't contain duplicates
        assertEquals(new HashSet<>(result).size(), result.size());
    }
}
