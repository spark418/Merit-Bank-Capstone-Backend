package com.meritamerica.assignment7.models;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.meritamerica.assignment7.enumerations.Money;
import com.meritamerica.assignment7.exceptions.NegativeAmountException;

@MappedSuperclass
public  abstract class Transaction{
	
	
	private Money type;
	private double amount;
	private double postedBalance;
	private java.util.Date date;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "savingsAccount")
	@JsonIgnore
	private SavingsAccount savingsAccount;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "checkingAccount")
	@JsonIgnore
	private CheckingAccount checkingAccount;
	
	public Transaction() {}
	
	public Transaction(double amount, double postedBalance, Money type){
		this.amount = amount;
		this.postedBalance = postedBalance;
		this.type = type;
		date = new java.util.Date();
	}
	
	public abstract void process() throws NegativeAmountException;

	public Money getType() {
		return type;
	}

	public void setType(Money type) {
		this.type = type;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getPostedBalance() {
		return postedBalance;
	}

	public void setPostedBalance(double postedBalance) {
		this.postedBalance = postedBalance;
	}
	
	public SavingsAccount getSavingsAccount() {
		return savingsAccount;
	}

	public void setSavingsAccount(SavingsAccount savingsAccount) {
		this.savingsAccount = savingsAccount;
	}

	public CheckingAccount getCheckingAccount() {
		return checkingAccount;
	}

	public void setCheckingAccount(CheckingAccount checkingAccount) {
		this.checkingAccount = checkingAccount;
	}
}
