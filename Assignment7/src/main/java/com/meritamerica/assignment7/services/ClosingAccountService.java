package com.meritamerica.assignment7.services;

import com.meritamerica.assignment7.exceptions.NoResourceFoundException;

public interface ClosingAccountService {
	
	public String closeCheckingAccount(int id, long num)throws NoResourceFoundException;
}
