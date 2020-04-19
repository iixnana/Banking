package io.bankbridge.TestUtil.Http;

import com.fasterxml.jackson.databind.ObjectMapper;
import spark.utils.IOUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.fail;

public class HttpClient {
    private final String host;
    private final int port;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public HttpClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public HttpResponse request(String method, String endpoint) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(
                    "http",
                    this.host,
                    this.port,
                    endpoint
            ).openConnection();
            connection.setRequestMethod(method);
            connection.setDoOutput(true);
            connection.connect();
            String body = IOUtils.toString(connection.getInputStream());
            if (isBodyJsonList(body)){
                return new HttpResponseJsonList(connection.getResponseCode(), body);
            } else if (isBodyJsonHashMap(body)) {
                return new HttpResponseJsonHashMap(connection.getResponseCode(), body);
            } else {
                throw new RuntimeException("Not implemented.");
            }
        } catch (IOException | RuntimeException e) {
            fail("Could not fulfill the request: " + e.getMessage());
            return null;
        }
    }

    private boolean isBodyJsonList(String body) {
        try {
            objectMapper.readValue(body, List.class);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private boolean isBodyJsonHashMap(String body) {
        try {
            objectMapper.readValue(body, HashMap.class);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
