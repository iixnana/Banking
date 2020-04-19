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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
		//Concurrent async calls to external servers
		List<CompletableFuture<Map<String, String>>> apiResultsFutures = config
				.entrySet()
				.stream()
				.map(entry -> CompletableFuture.supplyAsync(() -> {
					HttpRequest apiRequest = HttpRequest.newBuilder().uri(URI.create(entry.getValue())).build();
					try {
						HttpResponse<String> res = client.send(apiRequest, HttpResponse.BodyHandlers.ofString());
						Map<String, String> responseJson = objectMapper.readValue(res.body(), Map.class);
						if (responseJson.keySet().contains("bic")) {
							Map<String, String> map = new HashMap<>();
							map.put("id", responseJson.get("bic"));
							map.put("name", entry.getKey());
							return map;
						}
					} catch (IOException | InterruptedException e) {
						throw new RuntimeException("Error while processing request");
					}
					return null; }))
				.collect(Collectors.toList());
		try {
			CompletableFuture.allOf(
					apiResultsFutures.toArray(new CompletableFuture[apiResultsFutures.size()])
			).orTimeout(10, TimeUnit.MINUTES).get();
			for (CompletableFuture<Map<String, String>> apiAsyncCallResult : apiResultsFutures) {
				result.add(apiAsyncCallResult.get());
			}
			return objectMapper.writeValueAsString(result);
		} catch (JsonProcessingException | InterruptedException | ExecutionException e) {
			throw new RuntimeException("Error while processing request");
		}
	}
}
