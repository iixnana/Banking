package io.bankbridge;

import static spark.Spark.get;
import static spark.Spark.port;

import io.bankbridge.handler.BanksCacheBased;
import io.bankbridge.handler.BanksRemoteCalls;

public class Main {

	public static void main(String[] args) {
		port(8080);

		BanksCacheBased banksCacheBased = new BanksCacheBased();
		BanksRemoteCalls banksRemoteCalls = new BanksRemoteCalls();
		
		get("/v1/banks/all", (request, response) -> banksCacheBased.handle(request, response));
		get("/v2/banks/all", (request, response) -> banksRemoteCalls.handle(request, response));
		get("/heartbeat", (request, response) -> "");
	}
}