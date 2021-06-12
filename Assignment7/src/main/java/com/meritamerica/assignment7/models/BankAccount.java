
package com.meritamerica.assignment7.models;

import java.time.LocalDateTime;
import java.util.*;
import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class BankAccount {

	private  double balance;
	private  double interestRate;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private  long accountNumber;
	private  LocalDateTime openingDate;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "sourceAccount")
	private List <Transaction> sourceTransactions = new ArrayList<>();
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "targetAccount")
	private List <Transaction> targetTransactions = new ArrayList<>();
	
	
	public BankAccount(){
	
	}
	public BankAccount(double balance, double interestRate) {
		this.balance=balance;
	
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

