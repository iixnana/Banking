package io.bankbridge.TestUtil.Http;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.fail;

public class HttpResponseJsonHashMap extends HttpResponse {
    private final String body;
    private final int status;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public HttpResponseJsonHashMap(int status, String body) {
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
            return objectMapper.readValue(this.body, HashMap.class);
        } catch (IOException e) {
            fail("Could not parse body as JSON. Error message: " + e.getMessage());
            return null;
        }
    }
}
