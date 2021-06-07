package com.meritamerica.assignment7.models;

import java.time.LocalDateTime;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BankAccount {

	private  double balance;
	private  double interestRate;
	private  long accountNumber;
	private  LocalDateTime openingDate;
	
	private static long nextAccountNumber = 1;
	
	public BankAccount(){
	
	}
	public BankAccount(double balance, double interestRate) {
		this.balance=balance;
		this.accountNumber= nextAccountNumber++;
		this.interestRate = interestRate;
		this.openingDate = LocalDateTime.now();
	}
	
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public double getInterestRate() {
		return interestRate;
	}
	public void setInterestRate(double interestRate) {
		this.interestRate = interestRate;
	}
	public long getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(long accountNumber) {
		this.accountNumber = accountNumber;
	}
	public LocalDateTime getOpeningDate() {
		return openingDate;
	}
	public void setOpeningDate(LocalDateTime openingDate) {
		this.openingDate = openingDate;
	}
}
