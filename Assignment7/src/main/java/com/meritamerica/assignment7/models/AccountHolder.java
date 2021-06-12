package com.meritamerica.assignment7.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.meritamerica.assignment7.models.AccountHoldersContactDetails;
import com.meritamerica.assignment7.models.CDAccount;
import com.meritamerica.assignment7.models.CheckingAccount;
import com.meritamerica.assignment7.models.SavingsAccount;
import com.meritamerica.assignment7.security.models.User;
@Entity
@Table(name="AccountHolder")
public class AccountHolder {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@NotBlank(message = "First Name is mandatory")
	private String firstName;
	private String middleName;
	@NotBlank(message = "Last Name is mandatory")
	private String lastName;
	@NotBlank(message = "SSN is mandatory")
	private String ssn;
	
	
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "accountHolder")
	private AccountHoldersContactDetails accountHolderContactDetails;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "accountHolder")
	private List<CheckingAccount> checkingAccountList = new ArrayList<>();
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "accountHolder")
	private List<SavingsAccount> savingsAccountList = new ArrayList<>();
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "accountHolder")
	private List<CDAccount> cdAccList = new ArrayList<>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "accountHolder")
	private List<DBACheckingAccount> dbaCheckingAccountList = new ArrayList<>();
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "accountHolder")
	private List<RegularIRAAccount> regularIRAAccountList = new ArrayList<>();
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "accountHolder")
	private List<RothIRAAccount> rothIRAAccountList = new ArrayList<>();
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "accountHolder")
	private List<RolloverIRAAccount> rolloverIRAAccountList = new ArrayList<>();

	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
	
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public AccountHolder(String firstName, String middleName, String lastName, String ssn, int id) {
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.ssn = ssn;
		//this.id = nextId++;
	}

	public AccountHolder() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}


	public AccountHoldersContactDetails getAccountHolderContactDetails() {
		return accountHolderContactDetails;
	}

	public void setAccountHolderContactDetails(AccountHoldersContactDetails accountHolderContactDetails) {
		this.accountHolderContactDetails = accountHolderContactDetails;
	}

	public List<CheckingAccount> getCheckingAccountList() {
		return checkingAccountList;
	}

	public void setCheckingAccountList(List<CheckingAccount> checkingAccountList) {
		this.checkingAccountList = checkingAccountList;
	}

	public List<SavingsAccount> getSavingsAccountList() {
		return savingsAccountList;
	}

	public void setSavingsAccountList(List<SavingsAccount> savingsAccountList) {
		this.savingsAccountList = savingsAccountList;
	}

	public List<CDAccount> getCdAccList() {
		return cdAccList;
	}

	public void setCdAccList(List<CDAccount> cdAccList) {
		this.cdAccList = cdAccList;
	}
	
	public List<DBACheckingAccount> getDbaCheckingAccountList() {
		return dbaCheckingAccountList;
	}

	public void setDbaCheckingAccountList(List<DBACheckingAccount> dbaCheckingAccountList) {
		this.dbaCheckingAccountList = dbaCheckingAccountList;
	}

	public List<RegularIRAAccount> getRegularIRAAccountList() {
		return regularIRAAccountList;
	}

	public void setRegularIRAAccountList(List<RegularIRAAccount> regularIRAAccountList) {
		this.regularIRAAccountList = regularIRAAccountList;
	}

	public List<RothIRAAccount> getRothIRAAccountList() {
		return rothIRAAccountList;
	}

	public void setRothIRAAccountList(List<RothIRAAccount> rothIRAAccountList) {
		this.rothIRAAccountList = rothIRAAccountList;
	}

	public List<RolloverIRAAccount> getRolloverIRAAccountList() {
		return rolloverIRAAccountList;
	}

	public void setRolloverIRAAccountList(List<RolloverIRAAccount> rolloverIRAAccountList) {
		this.rolloverIRAAccountList = rolloverIRAAccountList;
	}

	
	public CheckingAccount addCheckingAccount(double openingBalance) {
		CheckingAccount checking = null;
		if ((getSavingsBalance() + getCheckingBalance() + openingBalance) < 250000) {
			checking = new CheckingAccount(openingBalance);
			this.checkingAccountList.add(checking);
		}
		return checking;
	}
	
	public SavingsAccount addSavingsAccount(double openingBalance) {
		SavingsAccount savings = null;
		if ((getSavingsBalance() + getCheckingBalance() + openingBalance) < 250000) {
			savings = new SavingsAccount(openingBalance);
			this.savingsAccountList.add(savings);
		}
		return savings;
	}

	public CDAccount addCDAccount(CDAccount cdAccount) {
		this.cdAccList.add(cdAccount);
		return cdAccount;
	}
	
	public DBACheckingAccount addDBACheckingAccount(double openingBalance) {
		DBACheckingAccount dbachecking = new DBACheckingAccount(openingBalance);
		this.dbaCheckingAccountList.add(dbachecking);
		return dbachecking;
	}
	
	public RegularIRAAccount addRegularIRAAccount(double openingBalance) {
		RegularIRAAccount regularira = new RegularIRAAccount(openingBalance);
		this.regularIRAAccountList.add(regularira);
		return regularira;
	}
	
	public RolloverIRAAccount addRolloverIRAAccount(double openingBalance) {
		RolloverIRAAccount rolloverira = new RolloverIRAAccount(openingBalance);
		this.rolloverIRAAccountList.add(rolloverira);
		return rolloverira;
	}
	
	public RothIRAAccount addRothIRAAccount(double openingBalance) {
		RothIRAAccount rothira = new RothIRAAccount(openingBalance);
		this.rothIRAAccountList.add(rothira);
		return rothira;
	}
	
	public int getNumberOfCheckingAccounts() {
		int numberOfCheckingAccounts = checkingAccountList.size();
		return numberOfCheckingAccounts;
	}
	
	public int getNumberOfSavingsAccounts() {
		int numberOfSavingsAccount = savingsAccountList.size();
		return numberOfSavingsAccount;
	}
	
	public int getNumberOfCDAccounts() {
		int numberOfCDAccounts = cdAccList.size();
		return numberOfCDAccounts;
	}
	
	public int getNumberOfDBACheckingAccounts() {
		int numberOfDBACheckingAccounts = dbaCheckingAccountList.size();
		return numberOfDBACheckingAccounts;
	}
	
	public int getNumberOfRolloverIRAAccounts() {
		int numberOfRolloverIRAAccounts = rolloverIRAAccountList.size();
		return numberOfRolloverIRAAccounts;
	}
	
	public int getNumberOfRegularIRAAccounts() {
		int numberOfRegularIRAAccounts = regularIRAAccountList.size();
		return numberOfRegularIRAAccounts;
	}
	
	public int getNumberOfRothIRAAccounts() {
		int numberOfRothIRAAccounts = rothIRAAccountList.size();
		return numberOfRothIRAAccounts;
	}
	
	public double getCheckingBalance() {
		double checkingTotal = 0;
		for (int i = 0; i < checkingAccountList.size(); i++) {
			checkingTotal += checkingAccountList.get(i).getBalance();
		}
		return checkingTotal; 
	}
	
	public double getSavingsBalance() {
		double savingsTotal = 0;
		for (int i = 0; i < savingsAccountList.size(); i++) {
			savingsTotal += savingsAccountList.get(i).getBalance();
		}
		return savingsTotal; 
	}
	
	public double getCDBalance() {
		double cdTotal = 0;
		for (int i = 0; i < cdAccList.size(); i++) {
			cdTotal += cdAccList.get(i).getBalance();
		}
		return cdTotal; 
	}
	
	public double getDBACheckingBalance() {
		double dbaCheckingTotal = 0;
		for(int i=0; i< dbaCheckingAccountList.size();i++) {
			dbaCheckingTotal+= dbaCheckingAccountList.get(i).getBalance();
		}
		return dbaCheckingTotal;
	}
	
	public double getRolloverIRABalance() {
		double rolloverIRATotal = 0;
		for(int i=0; i< rolloverIRAAccountList.size();i++) {
			rolloverIRATotal+= rolloverIRAAccountList.get(i).getBalance();
		}
		return rolloverIRATotal;
	}
	
	public double getRegularIRABalance() {
		double regularIRATotal = 0;
		for(int i=0; i< regularIRAAccountList.size();i++) {
			regularIRATotal+= regularIRAAccountList.get(i).getBalance();
		}
		return regularIRATotal;
	}
	
	public double getRothIRABalance() {
		double rothIRATotal = 0;
		for(int i=0; i< rothIRAAccountList.size();i++) {
			rothIRATotal+= rothIRAAccountList.get(i).getBalance();
		}
		return rothIRATotal;
	}
	
	public double getCombinedBalance() {
		return (getCheckingBalance() + getSavingsBalance()  + getCDBalance() + getDBACheckingBalance()+
				getRolloverIRABalance()+getRegularIRABalance()+getRothIRABalance());
	}
	
	public int getTotalAccounts() {
		 return getNumberOfCDAccounts()+getNumberOfSavingsAccounts()+getNumberOfCheckingAccounts()+getNumberOfDBACheckingAccounts()+
				 getNumberOfRolloverIRAAccounts()+ getNumberOfRegularIRAAccounts()+getNumberOfRothIRAAccounts();
	 }

	
}
