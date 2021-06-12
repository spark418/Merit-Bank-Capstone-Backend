package com.meritamerica.assignment7.models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "AccountHoldersContactDetails")
public class AccountHoldersContactDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String phoneNum;
	private String email;
	private String address;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "accountholder_id")
	@JsonIgnore
	private AccountHolder accountHolder;

	public AccountHoldersContactDetails() {

	}

	public AccountHoldersContactDetails(int id,String phoneNum, String email, String address) {
		this.phoneNum = phoneNum;
		this.email = email;
		this.address = address;
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public AccountHolder getAccountHolder() {
		return accountHolder;
	}

	public void setAccountHolder(AccountHolder accountHolder) {
		this.accountHolder = accountHolder;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
