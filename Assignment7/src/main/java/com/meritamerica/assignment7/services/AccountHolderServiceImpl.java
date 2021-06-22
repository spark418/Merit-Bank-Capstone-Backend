package com.meritamerica.assignment7.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meritamerica.assignment7.exceptions.NoResourceFoundException;
import com.meritamerica.assignment7.exceptions.InvalidAccountDetailsException;
import com.meritamerica.assignment7.models.AccountHolder;
import com.meritamerica.assignment7.models.AccountHoldersContactDetails;
import com.meritamerica.assignment7.models.CDAccount;
import com.meritamerica.assignment7.models.CheckingAccount;
import com.meritamerica.assignment7.models.DBACheckingAccount;
import com.meritamerica.assignment7.models.RegularIRAAccount;
import com.meritamerica.assignment7.models.RolloverIRAAccount;
import com.meritamerica.assignment7.models.RothIRAAccount;
import com.meritamerica.assignment7.models.SavingsAccount;
import com.meritamerica.assignment7.repository.AccountHolderRepository;
import com.meritamerica.assignment7.repository.AccountHoldersContactDetailsRepository;
import com.meritamerica.assignment7.repository.SavingsAccountRepository;
import com.meritamerica.assignment7.security.models.User;
import com.meritamerica.assignment7.security.repository.UserRepository;

@Service
public class AccountHolderServiceImpl implements AccountHolderService {

	@Autowired
	private AccountHolderRepository accountHolderRepository;
	
	@Autowired
	private AccountHoldersContactDetailsRepository contactDetailsRepository;
	
	@Autowired
	private SavingsAccountRepository savingsAccountRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public AccountHolder addAccountHolder(AccountHolder accountHolder) throws InvalidAccountDetailsException  {
		if ((accountHolder.getFirstName() == null) || (accountHolder.getLastName() == null) ||(accountHolder.getSsn() == null)) {
			throw new InvalidAccountDetailsException("Invalid details");
		}
		return accountHolderRepository.save(accountHolder);
	}

	@Override
	public List<AccountHolder> getAccountHolders() {
		return accountHolderRepository.findAll();
	}

	@Override
	public List<CheckingAccount> getCheckingAccount(int accountHolderId) throws NoResourceFoundException {
		AccountHolder accountHolder = accountHolderRepository.findById(accountHolderId).orElse(null);
		if (accountHolder == null) {
			throw new NoResourceFoundException("Invalid id");
		}
		return accountHolder.getCheckingAccountList().stream().filter(t -> t.isOpen()).collect(Collectors.toList());
	}

	@Override
	public List<SavingsAccount> getSavingsAccount(int accountHolderId) throws NoResourceFoundException {
		AccountHolder accountHolder = accountHolderRepository.findById(accountHolderId).orElse(null);
		if (accountHolder == null) {
			throw new NoResourceFoundException("Invalid id");
		}
		//return accountHolder.getSavingsAccountList();
		//Optional<SavingsAccount> savings=savingsAccountRepository.findByIsOpen(true);
		
			return accountHolder.getSavingsAccountList().stream().filter(t -> t.isOpen()).collect(Collectors.toList());
	}

	@Override
	public List<CDAccount> getCDAccount(int accountHolderId) throws NoResourceFoundException {
		AccountHolder accountHolder = accountHolderRepository.findById(accountHolderId).orElse(null);
		if (accountHolder == null) {
			throw new NoResourceFoundException("Invalid id");
		}
		return accountHolder.getCdAccList().stream().filter(t -> t.isOpen()).collect(Collectors.toList());
	}

	@Override
	public AccountHolder getAccountHolderById(int accountHolderId) throws NoResourceFoundException {
		AccountHolder accountHolder = accountHolderRepository.findById(accountHolderId).orElse(null);
		if (accountHolder == null) {
			throw new NoResourceFoundException("Invalid id");
		}
		return accountHolder;
	}

	@Override
	public AccountHoldersContactDetails addContactDetails(int accountHolderId,
			AccountHoldersContactDetails contactDetails) throws NoResourceFoundException  {
		AccountHolder accountHolder = accountHolderRepository.findById(accountHolderId).orElse(null);
		if (accountHolder == null) {
			throw new NoResourceFoundException("Invalid id");
		}
		contactDetails.setAccountHolder(accountHolder);
		contactDetailsRepository.save(contactDetails);
		return contactDetails;
	}
	
	@Override
	public AccountHoldersContactDetails getContactDetails(int accountHolderId) throws NoResourceFoundException {
		AccountHolder accountHolder = accountHolderRepository.findById(accountHolderId).orElse(null);
		if (accountHolder == null) {
			throw new NoResourceFoundException("Invalid id");
		}
		return accountHolder.getAccountHolderContactDetails();
	}

	@Override
	public List<DBACheckingAccount> getDBACheckingAccount(int accountHolderId) throws NoResourceFoundException {
		AccountHolder accountHolder = accountHolderRepository.findById(accountHolderId).orElse(null);
		if (accountHolder == null) {
			throw new NoResourceFoundException("Invalid id");
		}
		return accountHolder.getDbaCheckingAccountList().stream().filter(t -> t.isOpen()).collect(Collectors.toList());
	}

	@Override
	public List<RegularIRAAccount> getRegularIRAAccount(int accountHolderId) throws NoResourceFoundException {
		AccountHolder accountHolder = accountHolderRepository.findById(accountHolderId).orElse(null);
		if (accountHolder == null) {
			throw new NoResourceFoundException("Invalid id");
		}
		return accountHolder.getRegularIRAAccountList().stream().filter(t -> t.isOpen()).collect(Collectors.toList());
	}

	@Override
	public List<RothIRAAccount> getRothIRAAccount(int accountHolderId) throws NoResourceFoundException {
		AccountHolder accountHolder = accountHolderRepository.findById(accountHolderId).orElse(null);
		if (accountHolder == null) {
			throw new NoResourceFoundException("Invalid id");
		}
		return accountHolder.getRothIRAAccountList().stream().filter(t -> t.isOpen()).collect(Collectors.toList());
	}

	@Override
	public List<RolloverIRAAccount> getRolloverIRAAccount(int accountHolderId) throws NoResourceFoundException {
		AccountHolder accountHolder = accountHolderRepository.findById(accountHolderId).orElse(null);
		if (accountHolder == null) {
			throw new NoResourceFoundException("Invalid id");
		}
		return accountHolder.getRolloverIRAAccountList().stream().filter(t -> t.isOpen()).collect(Collectors.toList());
	}
	@Override
	public AccountHoldersContactDetails updateContactDetails(
			AccountHoldersContactDetails accountHolderContact,int contactsId) throws NoResourceFoundException{
		AccountHoldersContactDetails currentAccountHoldersContact =  contactDetailsRepository.findById(contactsId).orElse(null);
		currentAccountHoldersContact.setAddress(accountHolderContact.getAddress());
		currentAccountHoldersContact.setEmail(accountHolderContact.getEmail());
		currentAccountHoldersContact.setPhoneNum(accountHolderContact.getPhoneNum());
		return contactDetailsRepository.save(currentAccountHoldersContact);
	}
	
	@Override
	public AccountHolder updateAccountHolder(AccountHolder accountHolder, int id) throws NoResourceFoundException{
		AccountHolder currentAccHolder = accountHolderRepository.findById(id).orElse(null);
		currentAccHolder.setFirstName(accountHolder.getFirstName());
		currentAccHolder.setLastName(accountHolder.getLastName());
		currentAccHolder.setMiddleName(accountHolder.getMiddleName());
		currentAccHolder.setSsn(accountHolder.getSsn());
		
		return accountHolderRepository.save(currentAccHolder);
	}

	@Override
	public List<User> getUsers() {
		// TODO Auto-generated method stub
		return userRepository.findAll();
	}

}
