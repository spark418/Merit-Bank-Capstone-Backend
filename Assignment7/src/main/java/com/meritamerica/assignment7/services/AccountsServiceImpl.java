package com.meritamerica.assignment7.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meritamerica.assignment7.repository.CheckingAccountRepository;
import com.meritamerica.assignment7.repository.DBACheckingAccountRepository;
import com.meritamerica.assignment7.repository.RegularIRAAccountRepository;
import com.meritamerica.assignment7.repository.RolloverIRAAccountRepository;
import com.meritamerica.assignment7.repository.RothIRAAccountRepository;
import com.meritamerica.assignment7.repository.SavingsAccountRepository;
import com.meritamerica.assignment7.models.CDOffering;
import com.meritamerica.assignment7.exceptions.ExceedsCombinedBalanceLimitException;
import com.meritamerica.assignment7.exceptions.ExceedsNumberOfAccountsLimitException;
import com.meritamerica.assignment7.exceptions.NegativeAmountException;
import com.meritamerica.assignment7.exceptions.NoResourceFoundException;
import com.meritamerica.assignment7.models.AccountHolder;
import com.meritamerica.assignment7.models.BankAccount;
import com.meritamerica.assignment7.models.CDAccount;
import com.meritamerica.assignment7.models.CDAccountDTO;
import com.meritamerica.assignment7.models.CheckingAccount;
import com.meritamerica.assignment7.models.DBACheckingAccount;
import com.meritamerica.assignment7.models.RegularIRAAccount;
import com.meritamerica.assignment7.models.RolloverIRAAccount;
import com.meritamerica.assignment7.models.RothIRAAccount;
import com.meritamerica.assignment7.models.SavingsAccount;
import com.meritamerica.assignment7.repository.AccountHolderRepository;
import com.meritamerica.assignment7.repository.CDAccountRepository;
import com.meritamerica.assignment7.repository.CDOfferingRepository;

@Service
public class AccountsServiceImpl implements AccountsService {

	@Autowired
	AccountHolderRepository accountHolderRepository;
	
	@Autowired
	CheckingAccountRepository checkingAccountRepository;
	
	@Autowired
	SavingsAccountRepository savingsAccountRepository;
	
	@Autowired
	CDAccountRepository cdAccountRepository;
	
	@Autowired
	CDOfferingRepository cdOfferingRepository;
	
	@Autowired
	DBACheckingAccountRepository dbaCheckingAccountRepository;
	
	@Autowired
	RolloverIRAAccountRepository rolloverIRAAccountRepository;
	
	@Autowired
	RothIRAAccountRepository rothIRAAccountRepository;
	
	@Autowired
	RegularIRAAccountRepository regularIRAAccountRepository;
	
	@Override
	public CheckingAccount addCheckingAccount(int accountHolderId, CheckingAccount checkingAccount)throws NoResourceFoundException, NegativeAmountException, 
	ExceedsCombinedBalanceLimitException,ExceedsNumberOfAccountsLimitException {
		
		
		if(checkingAccount.getBalance()<0) {
			throw new NegativeAmountException();
		} 	
			
		AccountHolder accountHolder = accountHolderRepository.findById(accountHolderId).orElse(null);
		if(accountHolder==null) {
			throw new NoResourceFoundException("Invalid id");
		}
		if(accountHolder.getNumberOfCheckingAccounts()==1) {
			throw new ExceedsNumberOfAccountsLimitException("Only 1 Personal Checking Account permitted");
		}
		if (accountHolder.getCombinedBalance()+checkingAccount.getBalance()>250000) {
			throw new ExceedsCombinedBalanceLimitException("exceeds limit of amount 250,000 max");
		}
	
		CheckingAccount ch= new CheckingAccount(checkingAccount.getBalance());
		ch.setAccountHolder(accountHolder);
		return checkingAccountRepository.save(ch);
	}

	@Override
	public SavingsAccount addSavingsAccount(int accountHolderId, SavingsAccount savingsAccount) throws NoResourceFoundException, NegativeAmountException, 
	ExceedsCombinedBalanceLimitException,ExceedsNumberOfAccountsLimitException {
		if(savingsAccount.getBalance()<0) {
			throw new NegativeAmountException();
		} 	
			
		AccountHolder accountHolder = accountHolderRepository.findById(accountHolderId).orElse(null);
		if(accountHolder==null) {
			throw new NoResourceFoundException("Invalid id");
		}
		if(accountHolder.getNumberOfSavingsAccounts()== 1) {
			throw new ExceedsNumberOfAccountsLimitException("Only 1 Savings Account permitted");
		}
		if (accountHolder.getCombinedBalance()+savingsAccount.getBalance()>250000) {
			throw new ExceedsCombinedBalanceLimitException("exceeds limit of amount 250,000 max");
		}
	
		SavingsAccount sv= new SavingsAccount(savingsAccount.getBalance());
		sv.setAccountHolder(accountHolder);
		return savingsAccountRepository.save(sv);
	}

	@Override
	public CDAccount addCDAccount(int accountHolderId, CDAccountDTO cdAccDTO) throws NoResourceFoundException, NegativeAmountException {
		if(cdAccDTO.getBalance()<0) {
			throw new NegativeAmountException();
		} 	
			
		AccountHolder accountHolder = accountHolderRepository.findById(accountHolderId).orElse(null);
		if(accountHolder==null) {
			throw new NoResourceFoundException("Invalid id");
		}	
		CDOffering cdOffer=cdOfferingRepository.findById(cdAccDTO.getCdOffering().getId()).orElse(null);
		CDAccount cd= new CDAccount(cdOffer,cdAccDTO.getBalance());
		
		cd.setAccountHolder(accountHolder);
		return cdAccountRepository.save(cd);
	}

	@Override
	public DBACheckingAccount addDBACheckingAccount(int accountHolderId, DBACheckingAccount dbaCheckingAccount)
			throws NoResourceFoundException, NegativeAmountException, ExceedsCombinedBalanceLimitException,ExceedsNumberOfAccountsLimitException {
		if(dbaCheckingAccount.getBalance()<0) {
			throw new NegativeAmountException();
		} 	
			
		AccountHolder accountHolder = accountHolderRepository.findById(accountHolderId).orElse(null);
		if(accountHolder==null) {
			throw new NoResourceFoundException("Invalid id");
		}
		if(accountHolder.getNumberOfDBACheckingAccounts()==3) {
			throw new ExceedsNumberOfAccountsLimitException("Only maximum of 3 DBA Checking Accounts permitted");
		}
//		if (accountHolder.getCombinedBalance()+dbaCheckingAccount.getBalance()>250000) {
//			throw new ExceedsCombinedBalanceLimitException("exceeds limit of amount 250,000 max");
//		}
	
		DBACheckingAccount ch= new DBACheckingAccount(dbaCheckingAccount.getBalance());
		ch.setAccountHolder(accountHolder);
		return dbaCheckingAccountRepository.save(ch);
	}

	@Override
	public RolloverIRAAccount addRolloverIRAAccount(int accountHolderId, RolloverIRAAccount rolloverIRAAccount)
			throws NoResourceFoundException, NegativeAmountException, ExceedsCombinedBalanceLimitException,ExceedsNumberOfAccountsLimitException {
		if(rolloverIRAAccount.getBalance()<0) {
			throw new NegativeAmountException();
		} 	
			
		AccountHolder accountHolder = accountHolderRepository.findById(accountHolderId).orElse(null);
		if(accountHolder==null) {
			throw new NoResourceFoundException("Invalid id");
		}
		if(accountHolder.getNumberOfRolloverIRAAccounts()==1) {
			throw new ExceedsNumberOfAccountsLimitException("Only 1 Rollover IRA Account permitted");
		}
//		if (accountHolder.getCombinedBalance()+rolloverIRAAccount.getBalance()>250000) {
//			throw new ExceedsCombinedBalanceLimitException("exceeds limit of amount 250,000 max");
//		}
	
		RolloverIRAAccount ira= new RolloverIRAAccount(rolloverIRAAccount.getBalance());
		ira.setAccountHolder(accountHolder);
		return rolloverIRAAccountRepository.save(ira);
	}

	@Override
	public RothIRAAccount addRothIRAAccount(int accountHolderId, RothIRAAccount rothIRAAccount)
			throws NoResourceFoundException, NegativeAmountException, ExceedsCombinedBalanceLimitException,ExceedsNumberOfAccountsLimitException {
		if(rothIRAAccount.getBalance()<0) {
			throw new NegativeAmountException();
		} 	
			
		AccountHolder accountHolder = accountHolderRepository.findById(accountHolderId).orElse(null);
		if(accountHolder==null) {
			throw new NoResourceFoundException("Invalid id");
		}
		if(accountHolder.getNumberOfRothIRAAccounts()==1) {
			throw new ExceedsNumberOfAccountsLimitException("Only 1 Roth IRA Account permitted");
		}
//		if (accountHolder.getCombinedBalance()+rothIRAAccount.getBalance()>250000) {
//			throw new ExceedsCombinedBalanceLimitException("exceeds limit of amount 250,000 max");
//		}
	
		RothIRAAccount ira= new RothIRAAccount(rothIRAAccount.getBalance());
		ira.setAccountHolder(accountHolder);
		return rothIRAAccountRepository.save(ira);
	}

	@Override
	public RegularIRAAccount addRegularIRAAccount(int accountHolderId, RegularIRAAccount regularIRAAccount)
			throws NoResourceFoundException, NegativeAmountException, ExceedsCombinedBalanceLimitException, ExceedsNumberOfAccountsLimitException {
		if(regularIRAAccount.getBalance()<0) {
			throw new NegativeAmountException();
		} 	
			
		AccountHolder accountHolder = accountHolderRepository.findById(accountHolderId).orElse(null);
		if(accountHolder==null) {
			throw new NoResourceFoundException("Invalid id");
		}
		if(accountHolder.getNumberOfRegularIRAAccounts()==1) {
			throw new ExceedsNumberOfAccountsLimitException("Only 1 Regular IRA Account permitted");
		}
//		if (accountHolder.getCombinedBalance()+regularIRAAccount.getBalance()>250000) {
//			throw new ExceedsCombinedBalanceLimitException("exceeds limit of amount 250,000 max");
//		}
	
		RegularIRAAccount ira= new RegularIRAAccount(regularIRAAccount.getBalance());
		ira.setAccountHolder(accountHolder);
		return regularIRAAccountRepository.save(ira);
	}
	
	@Override
	public CheckingAccount getCheckingAccount(int accountHolderId, long accountNum) {
		// TODO Auto-generated method stub
		AccountHolder accountHolder = accountHolderRepository.findById(accountHolderId).orElse(null);
		return accountHolder.getCheckingAccountList().get(0);
	}

	@Override
	public DBACheckingAccount getDBACheckingAccount(int accountHolderId, long accountNum) {
		// TODO Auto-generated method stub
		AccountHolder accountHolder = accountHolderRepository.findById(accountHolderId).orElse(null);
		for(int i = 0; i < accountHolder.getNumberOfDBACheckingAccounts(); i++) {
			if(accountNum == accountHolder.getDbaCheckingAccountList().get(i).getAccountNumber()) {
				return accountHolder.getDbaCheckingAccountList().get(i);
			}
		}
		return null;
	}

	@Override
	public SavingsAccount getSavingsAccount(int accountHolderId, long accountNum) {
		// TODO Auto-generated method stub
		AccountHolder accountHolder = accountHolderRepository.findById(accountHolderId).orElse(null);
		if(accountHolder.getNumberOfSavingsAccounts() == 1)
		   return accountHolder.getSavingsAccountList().get(0);
		else return null;
	}

	@Override
	public RolloverIRAAccount getRolloverIRAAccount(int accountHolderId, long accountNum) {
		// TODO Auto-generated method stub
		AccountHolder accountHolder = accountHolderRepository.findById(accountHolderId).orElse(null);
		if(accountHolder.getNumberOfRolloverIRAAccounts() == 1)
		   return accountHolder.getRolloverIRAAccountList().get(0);
		else return null;
	
	}

	@Override
	public RothIRAAccount getRothIRAAccount(int accountHolderId, long accountNum) {
		// TODO Auto-generated method stub
		AccountHolder accountHolder = accountHolderRepository.findById(accountHolderId).orElse(null);
		if(accountHolder.getNumberOfRothIRAAccounts() == 1)
		   return accountHolder.getRothIRAAccountList().get(0);
		else return null;
	}

	@Override
	public RegularIRAAccount getRegularIRAAccount(int accountHolderId, long accountNum) {
		// TODO Auto-generated method stub
		AccountHolder accountHolder = accountHolderRepository.findById(accountHolderId).orElse(null);
		if(accountHolder.getNumberOfRegularIRAAccounts() == 1)
		   return accountHolder.getRegularIRAAccountList().get(0);
		else return null;
	}

	@Override
	public CDAccount getCDAccount(int accountHolderId, long accountNum) {
		// TODO Auto-generated method stub
		AccountHolder accountHolder = accountHolderRepository.findById(accountHolderId).orElse(null);

		for(int i = 0; i < accountHolder.getNumberOfCDAccounts(); i++) {
			if(accountNum == accountHolder.getCdAccList().get(i).getAccountNumber()) {
				return accountHolder.getCdAccList().get(i);
			}
		}
		return null;
	}

	@Override
	public BankAccount findAccount(long accountNum, int accountHolderId) {
		// TODO Auto-generated method stub
		AccountHolder accountHolder = accountHolderRepository.findById(accountHolderId).orElse(null);
		BankAccount account = null;
		List<BankAccount> accounts = accountHolder.allAccounts();
		//accountHolder
		for(int i = 0; i < accounts.size(); i ++) {
			if(accounts.get(i).getAccountNumber() == accountNum) account = accounts.get(i);
		}
		return account;
	}
	

	
	

}
