package io.bankbridge.handler;
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

	private static Map config;

	public static void init() throws Exception {
		config = new ObjectMapper()
				.readValue(Thread.currentThread().getContextClassLoader().getResource("banks-v2.json"), Map.class);
	}

	public static String handle(Request request, Response response) {
		HttpClient client = HttpClient.newHttpClient();
		List<Map> result = new ArrayList<>();
		config.forEach((key, entry) -> {
			HttpRequest apiRequest = HttpRequest.newBuilder()
					.uri(URI.create((String) entry))
					.build();
			try {
				HttpResponse<String> res = client.send(apiRequest, HttpResponse.BodyHandlers.ofString());
				Map apiResult = new ObjectMapper().readValue(res.body(), Map.class);
				Map map = new HashMap<>();
				if (apiResult.keySet().contains("bic")) {
					map.put("id", apiResult.get("bic"));
					map.put("name", key);
					result.add(map);
				}
			} catch (Exception e) {
				throw new RuntimeException("Error while processing request");
			}
		});
		try {
			String resultAsString = new ObjectMapper().writeValueAsString(result);
			return resultAsString;
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Error while processing request");
		}
	}
}
