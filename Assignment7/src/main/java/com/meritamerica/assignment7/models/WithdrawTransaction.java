package com.meritamerica.assignment7.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.meritamerica.assignment7.enumerations.Money;
import com.meritamerica.assignment7.exceptions.NegativeAmountException;

@Entity(name = "WithdrawTransaction")
public class WithdrawTransaction extends Transaction {
	
	 @Id
     @GeneratedValue(strategy = GenerationType.AUTO)
	 private int id;
		
	public WithdrawTransaction() {
	      super();	
	}
	
	public WithdrawTransaction( double amount, double balance, Money type){
		super.setAmount(amount);
		super.setType(type);
		super.setPostedBalance(balance);
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