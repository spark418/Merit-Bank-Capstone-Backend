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
@Table(name="DBACheckingAccount")
public class DBACheckingAccount extends BankAccount {
	
	public static final double DBA_CHECKING_INTERESTRATE= 0.0001;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountholder_id")
	@JsonIgnore
	private AccountHolder accountHolder;
	
	public DBACheckingAccount() {
		super.setOpen(true);
	}
	public DBACheckingAccount(double balance) {
		super(balance,DBA_CHECKING_INTERESTRATE);
	}
	
	public AccountHolder getAccountHolder() {
		return accountHolder;
	}
	public void setAccountHolder(AccountHolder accountHolder) {
		this.accountHolder = accountHolder;
	}
	
}
