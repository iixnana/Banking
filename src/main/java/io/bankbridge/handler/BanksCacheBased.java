package io.bankbridge.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.bankbridge.model.Bank;
import io.bankbridge.model.Banks;
import spark.Request;
import spark.Response;

public class BanksCacheBased {
	private CacheManager cacheManager;
	private final ObjectMapper objectMapper = new ObjectMapper();

	public BanksCacheBased() {
		cacheManager = CacheManagerBuilder
				.newCacheManagerBuilder()
				.withCache(
						"banks",
						CacheConfigurationBuilder.newCacheConfigurationBuilder(
								String.class,
								String.class,
								ResourcePoolsBuilder.heap(10)))
				.build();
		cacheManager.init();
		Cache<String, String> cache = cacheManager.getCache("banks", String.class, String.class);
		try {
			Banks models = objectMapper.readValue(
					Thread.currentThread().getContextClassLoader().getResource("banks-v1.json"),
					Banks.class
			);
			for (Bank model : models.getBanks()) {
				cache.put(model.getBic(), model.getName());
			}
		} catch (IOException e) {
			throw new RuntimeException("Error while reading data");
		}
	}

	public String handle(Request request, Response response) {
		List<Map<String, String>> result = new ArrayList<>();
		cacheManager.getCache("banks", String.class, String.class).forEach(entry -> {
			Map<String, String> map = new HashMap<>();
			map.put("id", entry.getKey());
			map.put("name", entry.getValue());
			result.add(map);
		});
		try {
			return objectMapper.writeValueAsString(result);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Error while processing request");
		}
	}
}
