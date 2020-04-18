package io.bankbridge;

import spark.Service;

public class MockRemotes {
	private static Service http;

	public static void main(String[] args) throws Exception {
		http = Service.ignite().port(1234).threadPool(20);

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
		/*
		* Not sure if it is a mistake, but this response has wrong format (contained "name" instead of "bic")
		* So changed it here, but I added logic in BanksRemoteCalls to check if it contains "bic" field:
		* if it does not contain the field, it will skip the response.
		*/
		http.get("/bes", (request, response) -> "{\n" +
				"\"bic\":\"9870\",\n" +
				"\"countryCode\":\"PT\",\n" + 
				"\"auth\":\"SSL\"\n" + 
				"}");
	}

	public static void stop(){
		http.stop();
	}
}