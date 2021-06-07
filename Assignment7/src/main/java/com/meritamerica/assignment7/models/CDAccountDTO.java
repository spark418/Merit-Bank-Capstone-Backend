package com.meritamerica.assignment7.models;

import com.meritamerica.assignment7.models.CDOffering;

public class CDAccountDTO {

	private double balance;
	private CDOffering cdOffering;
	
	public CDAccountDTO() {
		
	}
	
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public CDOffering getCdOffering() {
		return cdOffering;
	}
	public void setCdOffering(CDOffering cdOffering) {
		this.cdOffering = cdOffering;
	}
	
}

