package com.meritamerica.assignment7.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meritamerica.assignment7.enumerations.TransactionType;
import com.meritamerica.assignment7.exceptions.NoResourceFoundException;
import com.meritamerica.assignment7.models.AccountHolder;
import com.meritamerica.assignment7.models.BankAccount;
import com.meritamerica.assignment7.models.CDAccount;
import com.meritamerica.assignment7.models.CheckingAccount;
import com.meritamerica.assignment7.models.DBACheckingAccount;
import com.meritamerica.assignment7.models.RegularIRAAccount;
import com.meritamerica.assignment7.models.RolloverIRAAccount;
import com.meritamerica.assignment7.models.RothIRAAccount;
import com.meritamerica.assignment7.models.SavingsAccount;
import com.meritamerica.assignment7.models.TransferTransaction;
import com.meritamerica.assignment7.repository.AccountHolderRepository;
import com.meritamerica.assignment7.repository.CDAccountRepository;
import com.meritamerica.assignment7.repository.CDOfferingRepository;
import com.meritamerica.assignment7.repository.CheckingAccountRepository;
import com.meritamerica.assignment7.repository.DBACheckingAccountRepository;
import com.meritamerica.assignment7.repository.RegularIRAAccountRepository;
import com.meritamerica.assignment7.repository.RolloverIRAAccountRepository;
import com.meritamerica.assignment7.repository.RothIRAAccountRepository;
import com.meritamerica.assignment7.repository.SavingsAccountRepository;
import com.meritamerica.assignment7.security.services.UserService;

@Service
public class ClosingAccountImpl implements ClosingAccountService{
	
	@Autowired
	AccountsService accountService;
	
	@Autowired
	TransactionService transactionService;
	
	@Autowired
	AccountHolderService accountHolderService;
	
	@Autowired
	CheckingAccountRepository checkingAccountRepository;
		
	@Autowired
	SavingsAccountRepository savingsAccountRepository;
	
	@Autowired
	CDAccountRepository cdAccountRepository;
	
//	@Autowired
//	CDOfferingRepository cdOfferingRepository;
	
	@Autowired
	DBACheckingAccountRepository dbaCheckingAccountRepository;
	
	@Autowired
	RolloverIRAAccountRepository rolloverIRAAccountRepository;
	
	@Autowired
	RothIRAAccountRepository rothIRAAccountRepository;
	
	@Autowired
	RegularIRAAccountRepository regularIRAAccountRepository;
	
	@Autowired
    UserService userService;
	
	
	@Autowired
	AccountHolderRepository accountHolderRepository;
	
	@Override
    public String closeCheckingAccount(int id) throws NoResourceFoundException {
        // TODO Auto-generated method stud
        AccountHolder accountHolder = accountHolderRepository.findById(id).orElse(null);
        CheckingAccount checking = accountHolder.getCheckingAccountList().get(0);
        SavingsAccount savings = accountHolder.getSavingsAccountList().get(0);
          
        TransferTransaction transaction = new TransferTransaction(checking.getBalance(), savings.getBalance() + checking.getBalance(), 
        checking.getBalance() + -checking.getBalance(), TransactionType.Transfer, checking, savings);
        savings.setBalance(savings.getBalance() + checking.getBalance());
        checking.setBalance(0);
        checking.setOpen(false);
        transactionService.addTransferTransaction(transaction, checking, savings);
        
        return "Checking Account is closed";
    }

	@Override
	public String closeDBAAccount(int id, long num) throws NoResourceFoundException {
		// TODO Auto-generated method stub
		AccountHolder accountHolder = accountHolderRepository.findById(id).orElse(null);
		DBACheckingAccount dba = findDBAByAccountNum(accountHolder.getDbaCheckingAccountList(), num);
		SavingsAccount savings = accountHolder.getSavingsAccountList().get(0);
		
		TransferTransaction transaction = new TransferTransaction(dba.getBalance(), savings.getBalance() + dba.getBalance(), 
		dba.getBalance() + -dba.getBalance(), TransactionType.Transfer, dba, savings);
		
		savings.setBalance(savings.getBalance() + dba.getBalance());
		dba.setBalance(0);
		dba.setOpen(false);
		transactionService.addTransferTransaction(transaction, dba, savings);
		
		return "DBA checking account with account number " + num + " is closed";
	}

	@Override
	public String closeCDAccount(int id, long num) throws NoResourceFoundException {
		AccountHolder accountHolder = accountHolderRepository.findById(id).orElse(null);
		CDAccount cd = findCDByAccountNum(accountHolder.getCdAccList(), num);
		SavingsAccount savings = accountHolder.getSavingsAccountList().get(0);
		
		TransferTransaction transaction = new TransferTransaction(cd.getBalance(), savings.getBalance() + cd.getBalance(), 
	    cd.getBalance() + -cd.getBalance(), TransactionType.Transfer, cd, savings);
		
		savings.setBalance(savings.getBalance() + cd.getBalance());
		cd.setBalance(0);
		cd.setOpen(false);
		transactionService.addTransferTransaction(transaction, cd, savings);
		
		return "CD account with account number " + num + " is closed";
	
	}

	@Override
	public String closeRegularIRAAccount(int id) throws NoResourceFoundException {
		// TODO Auto-generated method stub
		AccountHolder accountHolder = accountHolderRepository.findById(id).orElse(null);
		RegularIRAAccount regular = accountHolder.getRegularIRAAccountList().get(0);
		SavingsAccount savings = accountHolder.getSavingsAccountList().get(0);
		
		double toSavings = regular.getBalance() * 0.8;
		double toIRS = regular.getBalance() * 0.2;
		
		TransferTransaction transaction = new TransferTransaction(toSavings, savings.getBalance() + toSavings, 
		regular.getBalance() + -regular.getBalance(), TransactionType.Transfer, regular, savings);
		
		savings.setBalance(savings.getBalance() + toSavings);
		regular.setBalance(0);
		regular.setOpen(false);
		
		transactionService.addTransferTransaction(transaction, regular, savings);
		
		
		return "Regular IRA account is closed. " + toIRS +" went to the IRS, and " + toSavings + " went to your savings.";
	}

	@Override
    public String closeSavingsAccount(int id) throws NoResourceFoundException {
        // TODO Auto-generated method stub
        AccountHolder accountHolder = accountHolderRepository.findById(id).orElse(null);
        
        for(int i = 0; i < accountHolder.allAccounts().size(); i++) {
            if(accountHolder.getCdAccList().size() != 0)
              cdAccountRepository.delete( accountHolder.getCdAccList().remove(i));    
            if(accountHolder.getDbaCheckingAccountList().size() != 0)
              dbaCheckingAccountRepository.delete(accountHolder.getDbaCheckingAccountList().remove(i));
            if(accountHolder.getCheckingAccountList().size() != 0)
              checkingAccountRepository.delete(accountHolder.getCheckingAccountList().remove(i));
            if(accountHolder.getSavingsAccountList().size() != 0)
              savingsAccountRepository.delete(accountHolder.getSavingsAccountList().remove(i));
            if(accountHolder.getRegularIRAAccountList().size() != 0)
              regularIRAAccountRepository.delete(accountHolder.getRegularIRAAccountList().remove(i));
            if(accountHolder.getRolloverIRAAccountList().size() != 0)
              rothIRAAccountRepository.delete(accountHolder.getRothIRAAccountList().remove(i));
            if(accountHolder.getRolloverIRAAccountList().size() != 0)
              rolloverIRAAccountRepository.delete(accountHolder.getRolloverIRAAccountList().remove(i));
            
        }
        accountHolderRepository.delete(accountHolder);
        return "All of your Banking records has been deleted from this bank";
    }
	
	private DBACheckingAccount findDBAByAccountNum(List<DBACheckingAccount> accounts, long num) 
			throws NoResourceFoundException {
		
		for(int i = 0; i < accounts.size(); i++) {
			if(accounts.get(i).getAccountNumber() == num)
				return accounts.get(i);
		}
		throw new NoResourceFoundException("Invalid Account Number");
	}
	
	
	private CDAccount findCDByAccountNum(List<CDAccount> accounts, long num) 
			throws NoResourceFoundException {
		
		for(int i = 0; i < accounts.size(); i++) {
			if(accounts.get(i).getAccountNumber() == num)
				return accounts.get(i);
		}
		throw new NoResourceFoundException("Invalid Account Number");
	}

	@Override
	public String closeRolloverIRAAccount(int id) throws NoResourceFoundException {
		AccountHolder accountHolder = accountHolderRepository.findById(id).orElse(null);
		RolloverIRAAccount rollover = accountHolder.getRolloverIRAAccountList().get(0);
		SavingsAccount savings = accountHolder.getSavingsAccountList().get(0);
		
		double toSavings = rollover.getBalance() * 0.8;
		double toIRS = rollover.getBalance() * 0.2;
		
		TransferTransaction transaction = new TransferTransaction(toSavings, savings.getBalance() + toSavings, 
		rollover.getBalance() + -rollover.getBalance(), TransactionType.Transfer, rollover, savings);
		
		savings.setBalance(savings.getBalance() + toSavings);
		rollover.setBalance(0);
		rollover.setOpen(false);
		
		transactionService.addTransferTransaction(transaction, rollover, savings);
		
		return "Regular IRA account is closed. " + toIRS +" went to the IRS, and " + toSavings + " went to your savings.";
	}

	@Override
	public String closeRothIRAAccount(int id) throws NoResourceFoundException {
		// TODO Auto-generated method stub
		AccountHolder accountHolder = accountHolderRepository.findById(id).orElse(null);
		RothIRAAccount roth = accountHolder.getRothIRAAccountList().get(0);
		SavingsAccount savings = accountHolder.getSavingsAccountList().get(0);
		
		double toSavings = roth.getBalance() * 0.8;
		double toIRS = roth.getBalance() * 0.2;
		
		TransferTransaction transaction = new TransferTransaction(toSavings, savings.getBalance() + toSavings, 
		roth.getBalance() + -roth.getBalance(), TransactionType.Transfer, roth, savings);
		
		savings.setBalance(savings.getBalance() + toSavings);
		roth.setBalance(0);
		roth.setOpen(false);
		
		transactionService.addTransferTransaction(transaction, roth, savings);
		
		return "Regular IRA account is closed. " + toIRS +" went to the IRS, and " + toSavings + " went to your savings.";
	}

}
