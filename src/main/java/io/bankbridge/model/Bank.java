package io.bankbridge.model;

/**
 * Refactored this class to implement encapsulation of data. One of the main principles of OOP.
 */
public class Bank {
	private String bic;
	private String name;
	private String countryCode;
	private String auth;

	public Bank() { }

	public Bank(String bic, String name, String countryCode, String auth) {
		this.bic = bic;
		this.name = name;
		this.setCountryCode(countryCode);
		this.setAuth(auth);
	}

	public String getBic(){
		return this.bic;
	};

	public void setBic(String bic){
		this.bic = bic;
	};

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getAuth() {
		return auth;
	}

	public void setAuth(String auth) {
		this.auth = auth;
	}
}