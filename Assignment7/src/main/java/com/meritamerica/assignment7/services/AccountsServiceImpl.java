package com.meritamerica.assignment7.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meritamerica.assignment7.repository.CheckingAccountRepository;
import com.meritamerica.assignment7.repository.SavingsAccountRepository;
import com.meritamerica.assignment7.models.CDOffering;
import com.meritamerica.assignment7.exceptions.ExceedsCombinedBalanceLimitException;
import com.meritamerica.assignment7.exceptions.NegativeAmountException;
import com.meritamerica.assignment7.exceptions.NoResourceFoundException;
import com.meritamerica.assignment7.models.AccountHolder;
import com.meritamerica.assignment7.models.CDAccount;
import com.meritamerica.assignment7.models.CDAccountDTO;
import com.meritamerica.assignment7.models.CheckingAccount;
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

	@Override
	public CheckingAccount addCheckingAccount(int accountHolderId, CheckingAccount checkingAccount)throws NoResourceFoundException, NegativeAmountException, ExceedsCombinedBalanceLimitException {

		if(checkingAccount.getBalance()<0) {
			throw new NegativeAmountException();
		} 	
			
		AccountHolder accountHolder = accountHolderRepository.findById(accountHolderId).orElse(null);
		if(accountHolder==null) {
			throw new NoResourceFoundException("Invalid id");
		}
		if (accountHolder.getCombinedBalance()+checkingAccount.getBalance()>250000) {
			throw new ExceedsCombinedBalanceLimitException("exceeds limit of amount 250,000 max");
		}
	
		CheckingAccount ch= new CheckingAccount(checkingAccount.getBalance());
		ch.setAccountHolder(accountHolder);
		return checkingAccountRepository.save(ch);
	}

	@Override
	public SavingsAccount addSavingsAccount(int accountHolderId, SavingsAccount savingsAccount) throws NoResourceFoundException, NegativeAmountException, ExceedsCombinedBalanceLimitException {
		if(savingsAccount.getBalance()<0) {
			throw new NegativeAmountException();
		} 	
			
		AccountHolder accountHolder = accountHolderRepository.findById(accountHolderId).orElse(null);
		if(accountHolder==null) {
			throw new NoResourceFoundException("Invalid id");
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

}
