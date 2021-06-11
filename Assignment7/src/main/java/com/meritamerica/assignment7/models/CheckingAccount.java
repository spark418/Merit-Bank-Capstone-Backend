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

@Entity
@Table(name="CheckingAccount")
public class CheckingAccount extends BankAccount {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	public static final double CHECKING_INTERESTRATE= 0.0001;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountholder_id")
	@JsonIgnore
    private AccountHolder accountHolder;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "checkingAccount")
	private List<DepositTransaction> depositTransactions = new ArrayList<>();
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "checkingAccount")
	private List<WithdrawTransaction> withdrawTransactions = new ArrayList<>();
	
  
	public CheckingAccount() {
	
	}
	public CheckingAccount(double balance) {
		super(balance,CHECKING_INTERESTRATE);
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