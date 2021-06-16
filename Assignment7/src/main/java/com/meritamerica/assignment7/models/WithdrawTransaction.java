package com.meritamerica.assignment7.models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


import com.meritamerica.assignment7.enumerations.TransactionType;
import com.meritamerica.assignment7.exceptions.NegativeAmountException;

@Entity(name = "WithdrawTransaction")
public class WithdrawTransaction extends Transaction {
	
	 @Id
     @GeneratedValue(strategy = GenerationType.AUTO)
	 private int id;
		
	public WithdrawTransaction() {
	      super();	
	}
	
	public WithdrawTransaction( double amount, double balance, TransactionType type,  BankAccount account){
		super.setAmount(amount);
		super.setType(type);
		super.setPostedBalance(balance);
		super.setDate(new Date());
		super.setSourceAccount(account);
	}
	
	
	
    
	@Override
	public void process() throws NegativeAmountException{
		
		if (getAmount()<0){
			NegativeAmountException negativeAmount=new NegativeAmountException();
			throw negativeAmount;				
		} else if(getAmount() <= TRANSACTLIMIT) {
			//super.getTargetAccount().addTransaction(this);
		}
	
	}

	private static final double TRANSACTLIMIT = 1000.0;
}

