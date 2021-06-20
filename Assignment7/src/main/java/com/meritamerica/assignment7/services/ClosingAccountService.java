package com.meritamerica.assignment7.services;

import com.meritamerica.assignment7.exceptions.NoResourceFoundException;

public interface ClosingAccountService {
	
	public String closeCheckingAccount(int id)throws NoResourceFoundException;
	public String closeDBAAccount(int id, long num) throws NoResourceFoundException;
	public String closeCDAccount(int id, long num) throws NoResourceFoundException;
	public String closeRegularIRAAccount(int id) throws NoResourceFoundException;
	public String closeRolloverIRAAccount(int id) throws NoResourceFoundException;
	public String closeRothIRAAccount(int id) throws NoResourceFoundException;
	public String closeSavingsAccount(int id) throws NoResourceFoundException;
}
