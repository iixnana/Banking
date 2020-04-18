package io.bankbridge;

import com.google.gson.JsonSyntaxException;
import spark.utils.IOUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.Gson;

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
            return new TestResponse(connection.getResponseCode(), body);
        } catch (IOException e) {
            e.printStackTrace();
            fail("Could not fulfill the request: " + e.getMessage());
            return null;
        }
    }

    public static class TestResponse {
        private final String body;
        private final int status;

        public TestResponse(int status, String body) {
            this.status = status;
            this.body = body;
        }

        public int getStatus() {
            return this.status;
        }

        public String getBody() {
            return this.body;
        }

        public List<Map<String, String>> getJsonList() {
            try {
                return new Gson().fromJson(this.body, List.class);
            } catch (JsonSyntaxException e) {
                System.out.println(
                        "Could not parse body as JSON. Body is not a list, try using getJson() instead.\n" +
                        "Error message: " + e.getMessage()
                );
                return null;
            }
        }

        public Map<String, String> getJson() {
            try {
                return new Gson().fromJson(this.body, HashMap.class);
            } catch (JsonSyntaxException e) {
                System.out.println(
                        "Could not parse body as JSON. Body might be a list, try using getJsonList() instead.\n" +
                        "Error message: " + e.getMessage()
                );
                return null;
            }
        }
    }
}
