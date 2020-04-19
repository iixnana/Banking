package io.bankbridge;

import spark.utils.IOUtils;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.fail;

public class TestUtil {
    private final static String apiURLWithPort = "http://localhost:8080";

    public static TestResponse request(String method, String path) {
        try {
            URL url = new URL(apiURLWithPort + path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setDoOutput(true);
            connection.connect();
            String body = IOUtils.toString(connection.getInputStream());
            if (isBodyJsonList(body)){
                return new ResponseJsonList(connection.getResponseCode(), body);
            } else if (isBodyJsonHashMap(body)) {
                return new ResponseJsonHashMap(connection.getResponseCode(), body);
            } else {
                throw new RuntimeException("Not implemented.");
            }
        } catch (Exception e) {
            fail("Could not fulfill the request: " + e.getMessage());
            return null;
        }
    }

    private static boolean isBodyJsonList(String body) {
        try {
            new ObjectMapper().readValue(body, List.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean isBodyJsonHashMap(String body) {
        try {
            new ObjectMapper().readValue(body, HashMap.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static abstract class TestResponse {
        abstract int getStatus();
        abstract String getBody();
        abstract Object getJson();
    }

    private static class ResponseJsonList extends TestResponse {
        private final String body;
        private final int status;

        public ResponseJsonList(int status, String body) {
            this.status = status;
            this.body = body;
        }

        @Override
        public int getStatus() {
            return this.status;
        }

        @Override
        public String getBody() {
            return this.body;
        }

        @Override
        public List<Map<String, String>> getJson() {
            try {
                return new ObjectMapper().readValue(this.body, List.class);
            } catch (Exception e) {
                System.out.println("Could not parse body as JSON. Error message: " + e.getMessage());
                return null;
            }
        }
    }

    private static class ResponseJsonHashMap extends TestResponse {
        private final String body;
        private final int status;

        public ResponseJsonHashMap(int status, String body) {
            this.status = status;
            this.body = body;
        }

        @Override
        public int getStatus() {
            return this.status;
        }

        @Override
        public String getBody() {
            return this.body;
        }

        @Override
        public Map<String, String> getJson() {
            try {
                return new ObjectMapper().readValue(this.body, HashMap.class);
            } catch (Exception e) {
                System.out.println("Could not parse body as JSON. Error message: " + e.getMessage());
                return null;
            }
        }
    }
}