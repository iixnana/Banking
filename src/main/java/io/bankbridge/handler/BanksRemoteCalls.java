package io.bankbridge.handler;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import spark.Request;
import spark.Response;

public class BanksRemoteCalls {
	private Map<String, String> config;
	private final ObjectMapper objectMapper = new ObjectMapper();

	public BanksRemoteCalls() {
		try {
			config = objectMapper.readValue(
					Thread.currentThread().getContextClassLoader().getResource("banks-v2.json"),
					Map.class
			);
		} catch (IOException e) {
			throw new RuntimeException("Error while reading data");
		}
	}

	public String handle(Request request, Response response) {
		HttpClient client = HttpClient.newHttpClient();
		List<Map<String, String>> result = new ArrayList<>();
		Map<String, String> map = new HashMap<>();
		config.forEach((key, entry) -> {
			HttpRequest apiRequest = HttpRequest.newBuilder()
					.uri(URI.create(entry))
					.build();
			try {
				HttpResponse<String> res = client.send(apiRequest, HttpResponse.BodyHandlers.ofString());
				Map<String, String> apiResult = objectMapper.readValue(res.body(), Map.class);
				if (apiResult.keySet().contains("bic")) {
					map.put("id", apiResult.get("bic"));
					map.put("name", key);
					result.add(map);
				}
			} catch (IOException | InterruptedException e) {
				throw new RuntimeException("Error while processing request");
			}
		});
		try {
			return objectMapper.writeValueAsString(result);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Error while processing request");
		}
	}
}
