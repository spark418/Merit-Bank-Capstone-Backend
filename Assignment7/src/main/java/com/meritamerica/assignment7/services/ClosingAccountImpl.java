package com.meritamerica.assignment7.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meritamerica.assignment7.enumerations.TransactionType;
import com.meritamerica.assignment7.exceptions.NoResourceFoundException;
import com.meritamerica.assignment7.models.CheckingAccount;
import com.meritamerica.assignment7.models.SavingsAccount;
import com.meritamerica.assignment7.models.TransferTransaction;

@Service
public class ClosingAccountImpl implements ClosingAccountService{
	
	@Autowired
	AccountsService accountService;
	
	@Autowired
	TransactionService transactionService;
	
	@Autowired
	AccountHolderService accountHolderService;
	
	@Override
	public String closeCheckingAccount(int id, long num) throws NoResourceFoundException {
		// TODO Auto-generated method stub
		CheckingAccount checking = accountHolderService.getAccountHolderById(id).getCheckingAccountList().get(0);
		SavingsAccount savings = accountHolderService.getAccountHolderById(id).getSavingsAccountList().get(0);
		  
		TransferTransaction transaction = new TransferTransaction(checking.getBalance(), savings.getBalance() + checking.getBalance(), 
		checking.getBalance() + -checking.getBalance(), TransactionType.Transfer, checking, savings);
		transactionService.addTransferTransaction(transaction, checking, savings);   
		checking.setOpen(false);
		
		return "Checking Account is closed";
	}

}
