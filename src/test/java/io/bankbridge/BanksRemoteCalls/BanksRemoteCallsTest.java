package io.bankbridge.BanksRemoteCalls;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.*;
import static spark.Spark.awaitInitialization;
import static spark.Spark.stop;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.bankbridge.Main;
import io.bankbridge.TestUtil.Http.HttpClient;
import io.bankbridge.TestUtil.Http.HttpResponse;
import io.bankbridge.TestUtil.Mock.MockRemotes;
import io.bankbridge.TestUtil.SparkManager.BankbridgeSparkManager;
import io.bankbridge.TestUtil.SparkManager.MockRemotesSparkManager;
import io.bankbridge.handler.BanksCacheBased;
import io.bankbridge.handler.BanksRemoteCalls;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class BanksRemoteCallsTest {
    private BanksRemoteCalls banksRemoteCalls;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final static String host = "localhost";
    private final static String heartbeatRoute = "/heartbeat";

    @BeforeClass
    public static void setUp() {
        MockRemotesSparkManager.get(host, heartbeatRoute).startSparkAppIfNotRunning(1234);
    }

    @Before
    public void setUpBeforeTest() {
        banksRemoteCalls = new BanksRemoteCalls();
    }

    @AfterClass
    public static void tearDown() {
        MockRemotesSparkManager.get(host, heartbeatRoute).killServer();
    }

    @Test
    public void getAllBanksTest() {
        try {
            String result = banksRemoteCalls.handle(null, null);
            List<Map<String, String>> resultJson = objectMapper.readValue(result, List.class);
            assertNotNull(resultJson);
            for (Map<String, String> element : resultJson) {
                assertNotNull(element.get("id"));
                assertNotNull(element.get("name"));
            }
        } catch (IOException e) {
            fail("Failed to convert result to JSON: " + e.getMessage());
        }
    }
}