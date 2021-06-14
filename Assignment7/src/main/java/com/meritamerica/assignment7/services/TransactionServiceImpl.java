package com.meritamerica.assignment7.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meritamerica.assignment7.models.BankAccount;
import com.meritamerica.assignment7.models.DepositTransaction;
import com.meritamerica.assignment7.models.Transaction;
import com.meritamerica.assignment7.models.TransferTransaction;
import com.meritamerica.assignment7.models.WithdrawTransaction;
import com.meritamerica.assignment7.repository.AccountHolderRepository;
import com.meritamerica.assignment7.repository.DepositTransactionRepository;
import com.meritamerica.assignment7.repository.TransferTransactionRepository;
import com.meritamerica.assignment7.repository.WithdrawTransactionRepository;

@Service
public class TransactionServiceImpl implements TransactionService {
	
	@Autowired
	DepositTransactionRepository depositTransactionRepository;
	

	@Autowired
	WithdrawTransactionRepository withdrawTransactionRepository;
	
	@Autowired
	TransferTransactionRepository transferTransactionRepository;
	
	@Autowired
	AccountHolderRepository accountHolderRepository;

	@Override
	public DepositTransaction addDepositTransaction(DepositTransaction transaction, BankAccount account) {
		// TODO Auto-generated method stub
		
		depositTransactionRepository.save(transaction);
		account.addDepositTransaction(transaction.getAmount(), transaction.getType() , account.getTargetTransactions());
		//transaction.setDate(new Date());
		return transaction;
	}

	@Override 
	public WithdrawTransaction addWithdrawTransaction(WithdrawTransaction transaction, BankAccount account) {
		// TODO Auto-generated method stub
		withdrawTransactionRepository.save(transaction);
		
		//account.setBalance(account.getBalance() + transaction.getAmount());
		account.addWithdrawTransaction(transaction.getAmount(), transaction.getType(), account.getSourceTransactions());
		return transaction;
	}

	@Override
	public TransferTransaction addTransferTransaction(TransferTransaction transaction, BankAccount account1,
	BankAccount account2) {
		// TODO Auto-generated method stub
		transferTransactionRepository.save(transaction);
		account1.addTransferTransaction(transaction.getAmount(), transaction.getType(), account1.getSourceTransactions(), account2);
	    account2.addTransferTransaction(transaction.getAmount(), transaction.getType(), account2.getTargetTransactions(), account2);
		return transaction;
	}
	
	

	@Override
	public List <Transaction> getTransactions(BankAccount account) {
		// TODO Auto-generated method stub
		return account.getAllTransactions();
	}

}
