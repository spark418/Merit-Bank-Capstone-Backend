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
@Table(name="RolloverIRAAccount")
public class RolloverIRAAccount extends BankAccount{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	public static final double ROLLOVER_IRA_INTERESTRATE= 0.0025;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountholder_id")
	@JsonIgnore
	private AccountHolder accountHolder;
	
	public RolloverIRAAccount() {
		
	}
	public RolloverIRAAccount(double balance) {
		super(balance,ROLLOVER_IRA_INTERESTRATE);
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public AccountHolder getAccountHolder() {
		return accountHolder;
	}
	public void setAccountHolder(AccountHolder accountHolder) {
		this.accountHolder = accountHolder;
	}
	

}
