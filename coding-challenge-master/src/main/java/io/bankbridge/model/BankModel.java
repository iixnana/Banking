package io.bankbridge.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Refactored this class to implement encapsulation of data. One of the main principles of OOP.
 * Included JsonProperty annotations, so the class can be (de)serialized
 */
public class BankModel {
	@JsonProperty("bic")
	private String bic;
	@JsonProperty("name")
	private String name;
	@JsonProperty("countryCode")
	private String countryCode;
	@JsonProperty("auth")
	private String auth;

	public BankModel() { }

	public BankModel(String bic, String name, String countryCode, String auth) {
		this.bic = bic;
		this.name = name;
		this.setCountryCode(countryCode);
		this.setAuth(auth);
	}

	@JsonProperty("bic")
	public String getBic(){
		return this.bic;
	};

	@JsonProperty("bic")
	public void setBic(String bic){
		this.bic = bic;
	};

	@JsonProperty("name")
	public String getName() {
		return name;
	}

	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty("countryCode")
	public String getCountryCode() {
		return countryCode;
	}

	@JsonProperty("countryCode")
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	@JsonProperty("auth")
	public String getAuth() {
		return auth;
	}

	@JsonProperty("auth")
	public void setAuth(String auth) {
		this.auth = auth;
	}
}