package io.bankbridge.BanksCacheBased;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.bankbridge.handler.BanksCacheBased;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.*;

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
            //Expecting result to be not null
            assertNotNull(resultJson);
            //Expecting more than zero elements
            assertTrue(resultJson.size() > 0);
            //Check all keys and values for not null
            for (Map<String, String> element : resultJson) {
                assertNotNull(element.get("id"));
                assertNotNull(element.get("name"));
            }
            //Check if result doesn't contain duplicates
            assertEquals(new HashSet<>(resultJson).size(), resultJson.size());
        } catch (IOException e) {
            fail("Failed to convert result to JSON: " + e.getMessage());
        }
    }
}