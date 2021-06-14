
package com.meritamerica.assignment7.models;

import java.time.LocalDateTime;
import java.util.*;
import javax.persistence.*;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.meritamerica.assignment7.enumerations.TransactionType;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class BankAccount {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private  long accountNumber;
	private  double balance;
	private  double interestRate;
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
	
	public List<Transaction> getSourceTransactions() {
		return sourceTransactions;
	}
	public void setSourceTransactions(List<Transaction> transactions) {
		this.sourceTransactions = transactions;
	}
	public List<Transaction> getTargetTransactions() {
		return targetTransactions;
	}
	public void setTargetTransactions(List<Transaction> targetTransactions) {
		this.targetTransactions = targetTransactions;
	}
	public Transaction addDepositTransaction(double amount, TransactionType type, List<Transaction> transactions) {
		//this.setBalance(this.getBalance() + amount);
	    transactions.add(new DepositTransaction(amount, getBalance(), type, this));
	    return transactions.get(transactions.size()-1);
	}
	
	public Transaction addWithdrawTransaction(double amount, TransactionType type, List<Transaction> transactions) {
	   // this.setBalance(balance + amount);
	    transactions.add(new WithdrawTransaction(amount, getBalance(), type, this));
	    return transactions.get(transactions.size()-1);
	}
	
	public Transaction addTransferTransaction(double amount, TransactionType type, List<Transaction> transactions, BankAccount account) {
		//setBalance(getBalance() + amount);
		if(amount < 0) {
	      transactions.add(new TransferTransaction(amount, account.getBalance(),getBalance(), type, this, account));
	      return transactions.get(transactions.size()-1);
		}
		else {
			transactions.add(new TransferTransaction(amount, getBalance(), account.getBalance(), type, account, this));
		      return transactions.get(transactions.size()-1);
		}
	   
	}
	
	public Transaction getTransaction(double amount, double balance,TransactionType type, List<Transaction> transactions) {
		 Transaction transaction = null;
		 for(int i = 0; i < transactions.size(); i++) {
			 if(transactions.get(i).getAmount()==amount) {
			   if(transactions.get(i).getPostedBalance() == balance){
				   if(transactions.get(i).getType() == type)transaction = transactions.get(i);
			   }
			 }
		 }
	     return transaction;
	}
	@JsonIgnore
	public List <Transaction> getAllTransactions(){
		List<Transaction> merged = null;
		if(!sourceTransactions.isEmpty() || !targetTransactions.isEmpty())
			merged = new ArrayList<>(sourceTransactions);
			merged.addAll(targetTransactions);
		return merged;
	}

}

