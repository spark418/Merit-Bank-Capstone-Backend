package com.meritamerica.assignment7.services;

import com.meritamerica.assignment7.exceptions.ExceedsCombinedBalanceLimitException;
import com.meritamerica.assignment7.exceptions.NegativeAmountException;
import com.meritamerica.assignment7.exceptions.NoResourceFoundException;
import com.meritamerica.assignment7.models.CDAccount;
import com.meritamerica.assignment7.models.CDAccountDTO;
import com.meritamerica.assignment7.models.CheckingAccount;
import com.meritamerica.assignment7.models.SavingsAccount;

public interface AccountsService {
	public CheckingAccount addCheckingAccount(int accountHolderId, CheckingAccount checkingAccount) throws NoResourceFoundException, NegativeAmountException, ExceedsCombinedBalanceLimitException;
	public SavingsAccount addSavingsAccount(int accountHolderId, SavingsAccount savingsAccount) throws NoResourceFoundException, NegativeAmountException, ExceedsCombinedBalanceLimitException;
	public CDAccount addCDAccount(int accountHolderId, CDAccountDTO cdAccount) throws NoResourceFoundException, NegativeAmountException;
}
