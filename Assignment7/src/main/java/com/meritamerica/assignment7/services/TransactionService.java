package com.meritamerica.assignment7.services;

import java.util.*;

import com.meritamerica.assignment7.models.BankAccount;
import com.meritamerica.assignment7.models.DepositTransaction;
import com.meritamerica.assignment7.models.Transaction;
import com.meritamerica.assignment7.models.TransferTransaction;
import com.meritamerica.assignment7.models.WithdrawTransaction;

public interface TransactionService {
	public DepositTransaction addDepositTransaction(DepositTransaction transaction, BankAccount account);
	public WithdrawTransaction addWithdrawTransaction(WithdrawTransaction transaction, BankAccount account);
	public TransferTransaction addTransferTransaction(TransferTransaction transaction, BankAccount account1, BankAccount account2);
	public List <Transaction> getTransactions(BankAccount account);
}