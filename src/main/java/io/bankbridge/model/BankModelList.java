package io.bankbridge.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Refactored this class to implement encapsulation of data. One of the main principles of OOP.
 * Included JsonProperty annotations, so the class can be (de)serialized
 */
public class BankModelList {
	@JsonProperty("banks")
	private List<BankModel> banks;

	public BankModelList() { }

	public BankModelList(List<BankModel> banks) {
		this.banks = banks;
	}

	@JsonProperty("banks")
	public List<BankModel> getBanks() {
		return banks;
	}

	@JsonProperty("banks")
	public void setBanks(List<BankModel> banks) {
		this.banks = banks;
	}
}
