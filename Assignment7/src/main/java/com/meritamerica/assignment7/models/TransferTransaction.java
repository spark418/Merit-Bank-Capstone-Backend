package com.meritamerica.assignment7.models;

import java.util.Date;

import javax.persistence.Entity;

import com.meritamerica.assignment7.enumerations.TransactionAction;
import com.meritamerica.assignment7.enumerations.TransactionType;
import com.meritamerica.assignment7.exceptions.NegativeAmountException;
@Entity
public class TransferTransaction extends Transaction{
	
	private double targetBalance;
	private TransactionAction action;
	
	

	public TransferTransaction() {}

	public TransferTransaction(double amount, double targetBalance, double balance, TransactionType type, BankAccount source, BankAccount target) {
		// TODO Auto-generated constructor stub
		super.setAmount(amount);
		super.setPostedBalance(balance);
		super.setType(type);
		super.setSourceAccount(source);
		super.setTargetAccount(target);
		this.setTargetBalance(targetBalance);
		super.setDate(new Date());
		this.setAction(TransactionAction.Transfer);
	}
	
	public TransactionAction getAction() {
		return action;
	}

	public void setAction(TransactionAction action) {
		this.action = action;
	}
	

	public double getTargetBalance() {
		return targetBalance;
	}

	public void setTargetBalance(double targetBalance) {
		this.targetBalance = targetBalance;
	}

	@Override
	public void process() throws NegativeAmountException {
		// TODO Auto-generated method stub
		
	}
	
}