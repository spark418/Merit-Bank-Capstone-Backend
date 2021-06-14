package com.meritamerica.assignment7.models;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.meritamerica.assignment7.models.CDOffering;

@Entity
@Table(name="CDAccount")
public class CDAccount extends BankAccount {
	
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cdoffering_id")
	private  CDOffering cdOffering;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountholder_id")
	@JsonIgnore
	private  AccountHolder accountHolder;
	
	public CDAccount() {
		
	}
	
	public CDAccount(CDOffering cdOffering,  double balance) {
		super( balance,cdOffering.getInterestRate());
		this.cdOffering=cdOffering;
	}

	public AccountHolder getAccountHolder() {
		return accountHolder;
	}

	public void setAccountHolder(AccountHolder accountHolder) {
		this.accountHolder = accountHolder;
	}

	public CDOffering getCdOffering() {
		return cdOffering;
	}
	
	public void setCdOffering(CDOffering cdOffering) {
		this.cdOffering = cdOffering;
	}
	
}

