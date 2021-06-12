package com.meritamerica.assignment7.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.meritamerica.assignment7.enumerations.TransactionType;

@Entity
@Table(name="SavingsAccount")
public class SavingsAccount extends BankAccount {
	

	public static final double SAVINGS_INTERESTRATE = 0.01;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountholder_id")
	@JsonIgnore
    private AccountHolder accountHolder;
	
	public SavingsAccount() {
		
	}
	public SavingsAccount(double balance) {
		super(balance, SAVINGS_INTERESTRATE);
	}
	
	public AccountHolder getAccountHolder() {
		return accountHolder;
	}
	public void setAccountHolder(AccountHolder accountHolder) {
		this.accountHolder = accountHolder;
	}
	/*
	public List<DepositTransaction> getTransactions() {
		return depositTransactions;
	}
	
	public void setTransactions(List<DepositTransaction> transactions) {
		this.depositTransactions = transactions;
	}
	
	public List<WithdrawTransaction> getWithdrawTransactions() {
		return withdrawTransactions;
	}
	public void setWithdrawTransactions(List<WithdrawTransaction> withdrawTransactions) {
		this.withdrawTransactions = withdrawTransactions;
	}
	
	public Transaction addDepositTransaction(double amount, TransactionType type) {
		super.setBalance(super.getBalance() + amount);
	   depositTransactions.add(new DepositTransaction(amount, super.getBalance(), type));
	    return depositTransactions.get(depositTransactions.size()-1);
	}
	
	public Transaction addWithdrawTransaction(double amount, TransactionType type) {
		super.setBalance(super.getBalance() + amount);
	    withdrawTransactions.add(new WithdrawTransaction(amount,  super.getBalance(), type));
	    return withdrawTransactions.get(withdrawTransactions.size()-1);
	}
	
	public Transaction getDepositTransaction(double amount, double balance, TransactionType type) {
		 Transaction transaction = null;
		 for(int i = 0; i < depositTransactions.size(); i++) {
			 if(depositTransactions.get(i).getAmount()==amount) {
			   if(depositTransactions.get(i).getPostedBalance() == balance){
				   if(depositTransactions.get(i).getType() == type)transaction = depositTransactions.get(i);
			   }
			 }
		 }
		 return transaction;
	}
	
	public Transaction getWithdrawTransaction(double amount, double balance, TransactionType type ) {
		 Transaction transaction = null;
		 for(int i = 0; i < withdrawTransactions.size(); i++) {
			 if(withdrawTransactions.get(i).getAmount()==amount) {
			   if(withdrawTransactions.get(i).getPostedBalance() == balance){
				   if(withdrawTransactions.get(i).getType() == type)transaction = withdrawTransactions.get(i);
			   }
			 }
		 }
		 return transaction;
	}
*/
	
	
	
}

