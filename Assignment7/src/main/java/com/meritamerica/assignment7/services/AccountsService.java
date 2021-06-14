package com.meritamerica.assignment7.services;

import com.meritamerica.assignment7.exceptions.ExceedsCombinedBalanceLimitException;
import com.meritamerica.assignment7.exceptions.ExceedsNumberOfAccountsLimitException;
import com.meritamerica.assignment7.exceptions.NegativeAmountException;
import com.meritamerica.assignment7.exceptions.NoResourceFoundException;
import com.meritamerica.assignment7.models.BankAccount;
import com.meritamerica.assignment7.models.CDAccount;
import com.meritamerica.assignment7.models.CDAccountDTO;
import com.meritamerica.assignment7.models.CheckingAccount;
import com.meritamerica.assignment7.models.DBACheckingAccount;
import com.meritamerica.assignment7.models.RegularIRAAccount;
import com.meritamerica.assignment7.models.RolloverIRAAccount;
import com.meritamerica.assignment7.models.RothIRAAccount;
import com.meritamerica.assignment7.models.SavingsAccount;

public interface AccountsService {
	
	public CheckingAccount getCheckingAccount(int accountHolderId, long accountNum);
	public DBACheckingAccount getDBACheckingAccount(int accountHolderId, long accountNum);
	public SavingsAccount getSavingsAccount(int accountHolderId, long accountNum);
	public CDAccount getCDAccount(int accountHolderId, long accountNum);
	public RolloverIRAAccount getRolloverIRAAccount(int accountHolderId, long accountNum);
	public RothIRAAccount getRothIRAAccount(int accountHolderId, long accountNum);
	public RegularIRAAccount getRegularIRAAccount(int accountHolderId, long accountNum);
	
	public CheckingAccount addCheckingAccount(int accountHolderId, CheckingAccount checkingAccount) throws NoResourceFoundException, NegativeAmountException, ExceedsCombinedBalanceLimitException,ExceedsNumberOfAccountsLimitException;
	public DBACheckingAccount addDBACheckingAccount(int accountHolderId, DBACheckingAccount dbaCheckingAccount) throws NoResourceFoundException, NegativeAmountException, ExceedsCombinedBalanceLimitException,ExceedsNumberOfAccountsLimitException;
	public SavingsAccount addSavingsAccount(int accountHolderId, SavingsAccount savingsAccount) throws NoResourceFoundException, NegativeAmountException, ExceedsCombinedBalanceLimitException,ExceedsNumberOfAccountsLimitException;
	public CDAccount addCDAccount(int accountHolderId, CDAccountDTO cdAccount) throws NoResourceFoundException, NegativeAmountException;
	public RolloverIRAAccount addRolloverIRAAccount(int accountHolderId, RolloverIRAAccount rolloverIRAAccount) throws NoResourceFoundException, NegativeAmountException, ExceedsCombinedBalanceLimitException,ExceedsNumberOfAccountsLimitException;
	public RothIRAAccount addRothIRAAccount(int accountHolderId, RothIRAAccount rothIRAAccount) throws NoResourceFoundException, NegativeAmountException, ExceedsCombinedBalanceLimitException,ExceedsNumberOfAccountsLimitException;
	public RegularIRAAccount addRegularIRAAccount(int accountHolderId, RegularIRAAccount regularIRAAccount) throws NoResourceFoundException, NegativeAmountException, ExceedsCombinedBalanceLimitException,ExceedsNumberOfAccountsLimitException;
	
	public BankAccount findAccount(long accountNum, int accountHolderId);
}
