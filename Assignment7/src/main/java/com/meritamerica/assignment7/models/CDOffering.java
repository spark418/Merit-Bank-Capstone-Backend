package com.meritamerica.assignment7.models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;



import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name = "CDOffering")
public class CDOffering {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private static int nextId = 1;
	private int term;
	private double interestRate;
	
	public CDOffering() {

	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "cdOffering")
	private List<CDAccount> cdAccounts;

	@JsonIgnore
	public List<CDAccount> getCdAccounts() {
		return cdAccounts;
	}

	public void setCdAccounts(List<CDAccount> cdAccounts) {
		this.cdAccounts = cdAccounts;
	}

	public CDOffering(int term, double interestRate) {
		this.term = term;
		this.interestRate = interestRate;
		this.id = nextId++;
	}

	public int getTerm() {
		return this.term;
	}

	public double getInterestRate() {
		return this.interestRate;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
