package io.bankbridge.BanksCacheBased;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.*;
import static spark.Spark.awaitInitialization;
import static spark.Spark.stop;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.bankbridge.Main;
import io.bankbridge.TestUtil.Http.HttpClient;
import io.bankbridge.TestUtil.Http.HttpResponse;
import io.bankbridge.TestUtil.SparkManager.BankbridgeSparkManager;
import io.bankbridge.handler.BanksCacheBased;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BanksCacheBasedTest {
    private BanksCacheBased banksCacheBased;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setUp() {
        banksCacheBased = new BanksCacheBased();
    }

    @Test
    public void getAllBanksTest() {
        try {
            String result = banksCacheBased.handle(null, null);
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