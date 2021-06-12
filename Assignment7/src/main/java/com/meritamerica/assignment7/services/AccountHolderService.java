package com.meritamerica.assignment7.services;

import java.util.List;

import com.meritamerica.assignment7.exceptions.InvalidAccountDetailsException;
import com.meritamerica.assignment7.exceptions.NoResourceFoundException;
import com.meritamerica.assignment7.models.AccountHolder;
import com.meritamerica.assignment7.models.AccountHoldersContactDetails;
import com.meritamerica.assignment7.models.CDAccount;
import com.meritamerica.assignment7.models.CheckingAccount;
import com.meritamerica.assignment7.models.DBACheckingAccount;
import com.meritamerica.assignment7.models.RegularIRAAccount;
import com.meritamerica.assignment7.models.RolloverIRAAccount;
import com.meritamerica.assignment7.models.RothIRAAccount;
import com.meritamerica.assignment7.models.SavingsAccount;

public interface AccountHolderService {
	public AccountHolder addAccountHolder(AccountHolder accountHolder) throws InvalidAccountDetailsException;
	public List<AccountHolder> getAccountHolders();
	public List<CheckingAccount> getCheckingAccount(int accountHolderId) throws NoResourceFoundException;
	public List<SavingsAccount> getSavingsAccount(int accountHolderId) throws NoResourceFoundException;
	public List<CDAccount> getCDAccount(int accountHolderId) throws NoResourceFoundException;
	public List<DBACheckingAccount> getDBACheckingAccount(int accountHolderId) throws NoResourceFoundException;
	public List<RegularIRAAccount> getRegularIRACheckingAccount(int accountHolderId) throws NoResourceFoundException;
	public List<RothIRAAccount> getRothIRAAccount(int accountHolderId) throws NoResourceFoundException;
	public List<RolloverIRAAccount> getRolloverIRAAccount(int accountHolderId) throws NoResourceFoundException;
	public AccountHolder getAccountHolderById(int accountHolderId) throws NoResourceFoundException;
	public AccountHoldersContactDetails addContactDetails(int accountHolderId, AccountHoldersContactDetails contactDetails) throws NoResourceFoundException;
	public AccountHoldersContactDetails getContactDetails(int accountHolderId) throws NoResourceFoundException;
}
