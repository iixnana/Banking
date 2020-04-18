package io.bankbridge;

import spark.Service;

public class MockRemotes {
	private static Service http;

	public static void main(String[] args) throws Exception {
		http = Service.ignite().port(1234).threadPool(10);

		http.get("/rbb", (request, response) -> "{\n" +
				"\"bic\":\"1234\",\n" + 
				"\"countryCode\":\"GB\",\n" + 
				"\"auth\":\"OAUTH\"\n" + 
				"}");
		http.get("/cs", (request, response) -> "{\n" +
				"\"bic\":\"5678\",\n" + 
				"\"countryCode\":\"CH\",\n" + 
				"\"auth\":\"OpenID\"\n" + 
				"}");
		http.get("/bes", (request, response) -> "{\n" +
				"\"name\":\"Banco de espiritu santo\",\n" + 
				"\"countryCode\":\"PT\",\n" + 
				"\"auth\":\"SSL\"\n" + 
				"}");
	}

	public static void stop(){
		http.stop();
	}
}