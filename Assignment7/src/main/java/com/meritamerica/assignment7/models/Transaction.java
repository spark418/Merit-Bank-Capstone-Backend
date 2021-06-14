package com.meritamerica.assignment7.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.meritamerica.assignment7.enumerations.TransactionType;
import com.meritamerica.assignment7.exceptions.NegativeAmountException;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public  abstract class Transaction{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected int id;
	private TransactionType type;
	private double amount;
	private double postedBalance;
	private java.util.Date date;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "target_account_id")
	@JsonIgnore
	private BankAccount targetAccount;
	
	

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "source_account_id")
	@JsonIgnore
	private BankAccount sourceAccount;
    
	public Transaction() {}
	
	public Transaction(double amount, double postedBalance, TransactionType type){
		this.amount = amount;
		this.postedBalance = postedBalance;
		this.type = type;
		date = new java.util.Date();
	}
	
	public Transaction(double amount, TransactionType type, BankAccount source){
		this.amount = amount;
		this.type = type;
		date = new java.util.Date();
	}
	
	public TransactionType getType() {
		return type;
	}

	public void setType(TransactionType type) {
		this.type = type;
	}

	public abstract void process() throws NegativeAmountException;

	

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
	
	public java.util.Date getDate() {
		return date;
	}

	public void setDate(java.util.Date date) {
		this.date = date;
	}
	
	public BankAccount getTargetAccount() {
		return targetAccount;
	}

	public void setTargetAccount(BankAccount targetAccount) {
		this.targetAccount = targetAccount;
	}

	public BankAccount getSourceAccount() {
		return sourceAccount;
	}

	public void setSourceAccount(BankAccount sourceAccount) {
		this.sourceAccount = sourceAccount;
	}

}

