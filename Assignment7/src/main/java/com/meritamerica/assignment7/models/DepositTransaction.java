package com.meritamerica.assignment7.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import com.meritamerica.assignment7.exceptions.NegativeAmountException;
import com.meritamerica.assignment7.enumerations.TransactionAction;
import com.meritamerica.assignment7.enumerations.TransactionType;


@Entity(name = "DepositTransaction")
public class DepositTransaction extends Transaction{
	
	private TransactionAction action;
	
    public DepositTransaction() {
      super();	
    }

	public DepositTransaction(double amount, double balance, TransactionType type, BankAccount target){
		super.setAmount(amount);
		super.setType(type);
		super.setPostedBalance(balance);
		super.setTargetAccount(target);
		super.setDate(new Date());
		this.setAction(TransactionAction.Deposit);
	}
	
	public TransactionAction getAction() {
		return action;
	}

	public void setAction(TransactionAction action) {
		this.action = action;
	}

	@Override
	public void process() throws NegativeAmountException{
		
			if (getAmount()<0){
				NegativeAmountException negativeAmount=new NegativeAmountException();
				throw negativeAmount;				
			} else if(getAmount()<=TRANSACTLIMIT) {
				//super.getTargetAccount().addTransaction(this);
			}
	}
	
	
	
	private static final double TRANSACTLIMIT = 1000.0;

}
