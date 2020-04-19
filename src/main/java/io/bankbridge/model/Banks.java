package io.bankbridge.model;

import java.util.List;

/**
 * Refactored this class to implement encapsulation of data. One of the main principles of OOP.
 */
public class Banks {
	private List<Bank> banks;

	public Banks() { }

	public Banks(List<Bank> banks) {
		this.banks = banks;
	}

	public List<Bank> getBanks() {
		return banks;
	}

	public void setBanks(List<Bank> banks) {
		this.banks = banks;
	}
}
