package com.meritamerica.assignment7.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;

import com.meritamerica.assignment7.enumerations.Money;
import com.meritamerica.assignment7.exceptions.NegativeAmountException;

@Entity(name = "DepositTransaction")
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class DepositTransaction extends Transaction{
	 
    
    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
    public DepositTransaction() {
      super();	
    }

	public DepositTransaction(double amount, double balance, Money type){
		super.setAmount(amount);
		super.setType(type);
		super.setPostedBalance(balance);
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
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
	private static final double TRANSACTLIMIT = 1000.0;
}