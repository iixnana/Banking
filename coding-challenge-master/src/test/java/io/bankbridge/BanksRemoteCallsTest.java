package io.bankbridge;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;
import static spark.Spark.awaitInitialization;
import static spark.Spark.stop;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class BanksRemoteCallsTest {
    @BeforeClass
    public static void setUp() throws Exception {
        String [] args = {};
        Main.main(args);
        awaitInitialization();
        MockRemotes.main(args);
    }

    @AfterClass
    public static void tearDown() {
        stop();
        MockRemotes.stop();
    }

    @Test
    public void getAllBanksTest() {
        String route = "/v2/banks/all";
        TestUtil.TestResponse response = TestUtil.request("GET", route);

        assertEquals(200, response.getStatus());
        List<Map<String, String>> result = (List<Map<String, String>>) response.getJson();
        assertNotNull(result);
        for (Map<String, String> element : result) {
            assertNotNull(element.get("id"));
            assertNotNull(element.get("name"));
        }
    }
}
