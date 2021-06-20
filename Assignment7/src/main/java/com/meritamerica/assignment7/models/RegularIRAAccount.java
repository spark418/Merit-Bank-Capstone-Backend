package com.meritamerica.assignment7.models;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="RegularIRAAccount")
public class RegularIRAAccount extends BankAccount{
	
	public static final double REGULAR_IRA_INTERESTRATE= 0.0025;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountholder_id")
	@JsonIgnore
	private AccountHolder accountHolder;
	
	public RegularIRAAccount() {
		
	}
	public RegularIRAAccount(double balance) {
		super(balance,REGULAR_IRA_INTERESTRATE);
		super.setOpen(true);
	}
	
	public AccountHolder getAccountHolder() {
		return accountHolder;
	}
	public void setAccountHolder(AccountHolder accountHolder) {
		this.accountHolder = accountHolder;
	}
	
}
