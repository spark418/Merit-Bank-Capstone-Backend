package com.meritamerica.assignment7.security.models;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.meritamerica.assignment7.models.AccountHolder;
import com.meritamerica.assignment7.models.AccountHoldersContactDetails;

@Entity
@Table(name = "User")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "Username is mandatory")
    private String userName;
    @NotBlank(message = "Enter the password")
    private String password;
    private boolean active;
    private String roles;
    
    
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user")
    @JsonIgnore
    private AccountHolder accountHolder;
    
    public User() {
	}

	public User(String userName, String password) {
		this.userName = userName;
		this.password = password;
		this.active = true;
		this.roles = "ROLE_USER";
	}

    public AccountHolder getAccountHolder() {
		return accountHolder;
	}

	public void setAccountHolder(AccountHolder accountHolder) {
		this.accountHolder = accountHolder;
	}

	public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }
}

