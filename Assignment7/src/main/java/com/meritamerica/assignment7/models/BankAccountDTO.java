package com.meritamerica.assignment7.models;

public class BankAccountDTO {
	
	private BankAccount account;
	
	public BankAccountDTO() {
		
	}
	
    public BankAccountDTO(BankAccount account) {
		this.account = account;
	}

	public BankAccount getAccount() {
		return account;
	}

	public void setAccount(BankAccount account) {
		this.account = account;
	}
}
