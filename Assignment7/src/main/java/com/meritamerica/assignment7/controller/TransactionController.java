package com.meritamerica.assignment7.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.meritamerica.assignment7.enumerations.TransactionType;
import com.meritamerica.assignment7.exceptions.NegativeAmountException;
import com.meritamerica.assignment7.exceptions.ZeroAmountException;
import com.meritamerica.assignment7.models.BankAccount;
import com.meritamerica.assignment7.models.CDAccount;
import com.meritamerica.assignment7.models.CheckingAccount;
import com.meritamerica.assignment7.models.DBACheckingAccount;
import com.meritamerica.assignment7.models.DepositTransaction;
import com.meritamerica.assignment7.models.RegularIRAAccount;
import com.meritamerica.assignment7.models.RolloverIRAAccount;
import com.meritamerica.assignment7.models.RothIRAAccount;
import com.meritamerica.assignment7.models.SavingsAccount;
import com.meritamerica.assignment7.models.Transaction;
import com.meritamerica.assignment7.models.TransferTransaction;
import com.meritamerica.assignment7.models.WithdrawTransaction;
import com.meritamerica.assignment7.services.AccountsService;
import com.meritamerica.assignment7.services.TransactionService;

@RestController
@CrossOrigin(origins = "*")
public class TransactionController {
	Logger logs = LoggerFactory.getLogger(TransactionController.class);
	
	@Autowired
	TransactionService transactionService;
	
	@Autowired
	AccountsService accountsService;
	
	//Transaction endpoints for savings account
	
	@PostMapping("/accountholder/{id}/savingsaccounts/{accNum}/deposittransaction")
	@ResponseStatus(HttpStatus.CREATED)
	@Secured("ROLE_ADMIN")
	public Transaction addDepositransactionToSavingsAccount(@PathVariable int id,@PathVariable long accNum ,
	@RequestBody TransactionDTO dto) throws NegativeAmountException, ZeroAmountException {
		SavingsAccount account = accountsService.getSavingsAccount(id, accNum);
		
		if(dto.getAmount() < 0) throw new NegativeAmountException();
		if(dto.getAmount() == 0) throw new ZeroAmountException("amount cannot be zero");
		
		DepositTransaction transaction =  new DepositTransaction(dto.getAmount(), 
		account.getBalance() + dto.getAmount(), TransactionType.valueOf(dto.getTransactionType()), account);
		account.setBalance(account.getBalance() + dto.getAmount());
	    return transactionService.addDepositTransaction(transaction, account);
	}
	
	@GetMapping("/accountholder/{id}/savingsaccounts/{accNum}/transactions")
	@ResponseStatus(HttpStatus.OK)
	@Secured("ROLE_ADMIN")
	public List <Transaction> getTransactionsFromSavings(@PathVariable int id,@PathVariable long accNum) {
		SavingsAccount account = accountsService.getSavingsAccount(id, accNum);
		return transactionService.getTransactions(account);
	}
	
	@PostMapping("/accountholder/{id}/savingsaccounts/{accNum}/withdrawtransaction")
	@ResponseStatus(HttpStatus.CREATED)
	@Secured("ROLE_ADMIN")
	public Transaction addWithdrawToSavings(@PathVariable int id,@PathVariable long accNum ,
	@RequestBody TransactionDTO dto) throws NegativeAmountException, ZeroAmountException {
		SavingsAccount account = accountsService.getSavingsAccount(id, accNum);
		//double balance = accountsService.getSavingsAccount(id, accNum).getBalance();
		
		if(dto.getAmount() < 0) throw new NegativeAmountException();
		if(account.getBalance() == 0) throw new ZeroAmountException("balance is zero");
		if(account.getBalance() - dto.getAmount() < 0) throw new ZeroAmountException("Invalid Transaction");
		if(dto.getAmount() == 0) throw new ZeroAmountException("Amount cannot be zero");
		
		WithdrawTransaction transaction = new WithdrawTransaction(dto.getAmount(), account.getBalance() + dto.getAmount(), 
	    TransactionType.valueOf(dto.getTransactionType()),  accountsService.getSavingsAccount(id, accNum));
		account.setBalance(account.getBalance() + dto.getAmount());
		return transactionService.addWithdrawTransaction(transaction,  account);
	}
	
	//transaction endpoints for checking account
	
	@PostMapping("/accountholder/{id}/checkingaccounts/{accNum}/deposittransaction")
	@ResponseStatus(HttpStatus.CREATED)
	@Secured("ROLE_ADMIN")
	public Transaction addDepositransactionToCheckingAccount(@PathVariable int id,@PathVariable long accNum ,
	@RequestBody TransactionDTO dto) throws NegativeAmountException, ZeroAmountException {
		CheckingAccount account = accountsService.getCheckingAccount(id, accNum);
		
		if(dto.getAmount() < 0) throw new NegativeAmountException();
		if(account.getBalance() == 0) throw new ZeroAmountException("balance is zero");
		
		DepositTransaction transaction =  new DepositTransaction(dto.getAmount(), 
		account.getBalance() + dto.getAmount(), TransactionType.valueOf(dto.getTransactionType()), account);
		account.setBalance(account.getBalance() + dto.getAmount());
	    return transactionService.addDepositTransaction(transaction, account);
	}
	
	@GetMapping("/accountholder/{id}/checkingaccounts/{accNum}/transactions")
	@ResponseStatus(HttpStatus.OK)
	@Secured("ROLE_ADMIN")
	public List <Transaction> getTransactionsFromCheckings(@PathVariable int id,@PathVariable long accNum) {
		CheckingAccount account = accountsService.getCheckingAccount(id, accNum);
		return transactionService.getTransactions(account);
	}
	
	@PostMapping("/accountholder/{id}/checkingaccounts/{accNum}/withdrawtransaction")
	@ResponseStatus(HttpStatus.CREATED)
	@Secured("ROLE_ADMIN")
	public Transaction addWithdrawToChecking(@PathVariable int id,@PathVariable long accNum ,
	@RequestBody TransactionDTO dto) throws NegativeAmountException, ZeroAmountException{
		CheckingAccount account = accountsService.getCheckingAccount(id, accNum);
		//double balance = accountsService.getSavingsAccount(id, accNum).getBalance();
		
		if(dto.getAmount() < 0) throw new NegativeAmountException();
		if(account.getBalance() == 0) throw new ZeroAmountException("balance is zero");
		if(account.getBalance() - dto.getAmount() < 0) throw new ZeroAmountException("Invalid Transaction");
		if(dto.getAmount() == 0) throw new ZeroAmountException("Amount cannot be zero");
		
		WithdrawTransaction transaction = new WithdrawTransaction(dto.getAmount(), account.getBalance() + dto.getAmount(), 
	    TransactionType.valueOf(dto.getTransactionType()),  accountsService.getCheckingAccount(id, accNum));
		account.setBalance(account.getBalance() + dto.getAmount());
		return transactionService.addWithdrawTransaction(transaction,  account);
	}
	
	//transaction endpoint for transfers
	
	@PostMapping("/accountholder/{id}/accounts/{sourceNum}/transfer/{targetNum}")
	@ResponseStatus(HttpStatus.CREATED)
	@Secured("ROLE_ADMIN")
	public Transaction addTransferToAccount(@PathVariable int id, @PathVariable long sourceNum, @PathVariable long targetNum, 
	@RequestBody TransactionDTO dto) throws NegativeAmountException, ZeroAmountException {
		BankAccount source = accountsService.findAccount(sourceNum, id); 
		BankAccount target = accountsService.findAccount(targetNum, id);
		
		if(dto.getAmount() < 0) throw new NegativeAmountException();
		if(source.getBalance() == 0) throw new ZeroAmountException("Invalid Transaction");
		
		TransferTransaction sourceTransaction = new TransferTransaction(dto.getAmount(), target.getBalance() + dto.getAmount(), 
		source.getBalance() + -dto.getAmount(),
		TransactionType.valueOf(dto.getTransactionType()), source, target);
		
		source.setBalance( source.getBalance() + -dto.getAmount());
		
		TransferTransaction targetTransaction = new TransferTransaction(dto.getAmount(), target.getBalance() + dto.getAmount(),
		source.getBalance() + -dto.getAmount(),
	    TransactionType.valueOf(dto.getTransactionType()), source, target);
		
		target.setBalance(target.getBalance() + dto.getAmount());
		
		return transactionService.addTransferTransaction(sourceTransaction, source, target);
	}
	
	
	//transaction endpoints for dba checking account
	
	@PostMapping("/accountholder/{id}/dbaaccounts/{accNum}/deposittransaction")
	@ResponseStatus(HttpStatus.CREATED)
	@Secured("ROLE_ADMIN")
	public Transaction addDepositransactionToDBAAccount(@PathVariable int id,@PathVariable long accNum ,
	@RequestBody TransactionDTO dto) throws NegativeAmountException, ZeroAmountException {
		DBACheckingAccount account = accountsService.getDBACheckingAccount(id, accNum);
		
		if(dto.getAmount() < 0) throw new NegativeAmountException();
		if(account.getBalance() == 0) throw new ZeroAmountException("balance is zero");
	
		
		
		DepositTransaction transaction =  new DepositTransaction(dto.getAmount(), 
		account.getBalance() + dto.getAmount(), TransactionType.valueOf(dto.getTransactionType()), account);
		account.setBalance(account.getBalance() + dto.getAmount());
	    return transactionService.addDepositTransaction(transaction, account);
	}
	
	@GetMapping("/accountholder/{id}/dbaaccounts/{accNum}/transactions")
	@ResponseStatus(HttpStatus.OK)
	@Secured("ROLE_ADMIN")
	public List <Transaction> getTransactionsFromDBA(@PathVariable int id,@PathVariable long accNum) {
		DBACheckingAccount account = accountsService.getDBACheckingAccount(id, accNum);
		return transactionService.getTransactions(account);
	}
	
	@PostMapping("/accountholder/{id}/dbaaccounts/{accNum}/withdrawtransaction")
	@ResponseStatus(HttpStatus.CREATED)
	@Secured("ROLE_ADMIN")
	public Transaction addWithdrawToDBA(@PathVariable int id,@PathVariable long accNum ,
	@RequestBody TransactionDTO dto) throws NegativeAmountException, ZeroAmountException {
		DBACheckingAccount account = accountsService.getDBACheckingAccount(id, accNum);
		//double balance = accountsService.getSavingsAccount(id, accNum).getBalance();
		
		if(dto.getAmount() < 0) throw new NegativeAmountException();
		if(account.getBalance() == 0) throw new ZeroAmountException("balance is zero");
		if(account.getBalance() - dto.getAmount() < 0) throw new ZeroAmountException("Invalid Transaction");
		if(dto.getAmount() == 0) throw new ZeroAmountException("Amount cannot be zero");
		
		
		
		WithdrawTransaction transaction = new WithdrawTransaction(dto.getAmount(), account.getBalance() + dto.getAmount(), 
	    TransactionType.valueOf(dto.getTransactionType()),  accountsService.getDBACheckingAccount(id, accNum));
		account.setBalance(account.getBalance() + dto.getAmount());
		return transactionService.addWithdrawTransaction(transaction,  account);
	}
	
	
	   //transaction endpoints for CD account
	
		@PostMapping("/accountholder/{id}/cdaccounts/{accNum}/deposittransaction")
		@ResponseStatus(HttpStatus.CREATED)
		@Secured("ROLE_ADMIN")
		public Transaction addDepositransactionToCDAccount(@PathVariable int id,@PathVariable long accNum ,
		@RequestBody TransactionDTO dto) throws NegativeAmountException, ZeroAmountException {
			CDAccount account = accountsService.getCDAccount(id, accNum);
			DepositTransaction transaction =  new DepositTransaction(dto.getAmount(), 
			account.getBalance() + dto.getAmount(), TransactionType.valueOf(dto.getTransactionType()), account);
			
			if(dto.getAmount() < 0) throw new NegativeAmountException();
			if(account.getBalance() == 0) throw new ZeroAmountException("balance is zero");
			
			
			account.setBalance(account.getBalance() + dto.getAmount());
		    return transactionService.addDepositTransaction(transaction, account);
		}
		
		@GetMapping("/accountholder/{id}/cdaccounts/{accNum}/transactions")
		@ResponseStatus(HttpStatus.OK)
		@Secured("ROLE_ADMIN")
		public List <Transaction> getTransactionsFromCDAccount(@PathVariable int id,@PathVariable long accNum) {
			CDAccount account = accountsService.getCDAccount(id, accNum);
			return transactionService.getTransactions(account);
		}
		
		@PostMapping("/accountholder/{id}/cdaccounts/{accNum}/withdrawtransaction")
		@ResponseStatus(HttpStatus.CREATED)
		@Secured("ROLE_ADMIN")
		public Transaction addWithdrawToCDAccount(@PathVariable int id,@PathVariable long accNum ,
		@RequestBody TransactionDTO dto) throws NegativeAmountException, ZeroAmountException {
			CDAccount account = accountsService.getCDAccount(id, accNum);
			//double balance = accountsService.getSavingsAccount(id, accNum).getBalance();
			
			if(dto.getAmount() < 0) throw new NegativeAmountException();
			if(account.getBalance() == 0) throw new ZeroAmountException("balance is zero");
			if(account.getBalance() - dto.getAmount() < 0) throw new ZeroAmountException("Invalid Transaction");
			if(dto.getAmount() == 0) throw new ZeroAmountException("Amount cannot be zero");
			
			WithdrawTransaction transaction = new WithdrawTransaction(dto.getAmount(), account.getBalance() + dto.getAmount(), 
		    TransactionType.valueOf(dto.getTransactionType()),  accountsService.getCDAccount(id, accNum));
			account.setBalance(account.getBalance() + dto.getAmount());
			return transactionService.addWithdrawTransaction(transaction,  account);
		}
		
		  //transaction endpointsfor Regular IRA Accounts
		
	
		@PostMapping("/accountholder/{id}/regularIRA/{accNum}/deposittransaction")
		@ResponseStatus(HttpStatus.CREATED)
		@Secured("ROLE_ADMIN")
		public Transaction addDepositransactionToRegularIRA(@PathVariable int id,@PathVariable long accNum ,
		@RequestBody TransactionDTO dto) throws NegativeAmountException, ZeroAmountException {
			
			RegularIRAAccount account = accountsService.getRegularIRAAccount(id, accNum);
			
			if(dto.getAmount() < 0) throw new NegativeAmountException();
			if(account.getBalance() == 0) throw new ZeroAmountException("balance is zero");
			
			DepositTransaction transaction =  new DepositTransaction(dto.getAmount(), 
			account.getBalance() + dto.getAmount(), TransactionType.valueOf(dto.getTransactionType()), account);
			account.setBalance(account.getBalance() + dto.getAmount());
			return transactionService.addDepositTransaction(transaction, account);
		}
			
			@GetMapping("/accountholder/{id}/regularIRA/{accNum}/transactions")
			@ResponseStatus(HttpStatus.OK)
			@Secured("ROLE_ADMIN")
			public List <Transaction> getTransactionsFromRegularIRA(@PathVariable int id,@PathVariable long accNum) {
				RegularIRAAccount account = accountsService.getRegularIRAAccount(id, accNum);
				return transactionService.getTransactions(account);
		}
			
			@PostMapping("/accountholder/{id}/regularIRA/{accNum}/withdrawtransaction")
			@ResponseStatus(HttpStatus.CREATED)
			@Secured("ROLE_ADMIN")
			public Transaction addWithdrawToRegularIRA(@PathVariable int id,@PathVariable long accNum ,
  		    @RequestBody TransactionDTO dto) throws NegativeAmountException, ZeroAmountException {
				RegularIRAAccount account = accountsService.getRegularIRAAccount(id, accNum);
				//double balance = accountsService.getSavingsAccount(id, accNum).getBalance();
				
				if(dto.getAmount() < 0) throw new NegativeAmountException();
				if(account.getBalance() == 0) throw new ZeroAmountException("balance is zero");
				if(account.getBalance() - dto.getAmount() < 0) throw new ZeroAmountException("Invalid Transaction");
				if(dto.getAmount() == 0) throw new ZeroAmountException("Amount cannot be zero");
				
				WithdrawTransaction transaction = new WithdrawTransaction(dto.getAmount(), account.getBalance() + dto.getAmount(), 
			    TransactionType.valueOf(dto.getTransactionType()),  accountsService.getRegularIRAAccount(id, accNum));
				account.setBalance(account.getBalance() + dto.getAmount());
				return transactionService.addWithdrawTransaction(transaction,  account);
		}
			
			
			//transaction endpointsfor Rollover IRA Accounts
			
			@PostMapping("/accountholder/{id}/rolloverIRA/{accNum}/deposittransaction")
			@ResponseStatus(HttpStatus.CREATED)
			@Secured("ROLE_ADMIN")
			public Transaction addDepositransactionToRolloverIRA(@PathVariable int id,@PathVariable long accNum ,
			@RequestBody TransactionDTO dto) throws NegativeAmountException, ZeroAmountException {
				
				RolloverIRAAccount account = accountsService.getRolloverIRAAccount(id, accNum);
				
				
				if(dto.getAmount() < 0) throw new NegativeAmountException();
				if(account.getBalance() == 0) throw new ZeroAmountException("balance is zero");
				
				DepositTransaction transaction =  new DepositTransaction(dto.getAmount(), 
				account.getBalance() + dto.getAmount(), TransactionType.valueOf(dto.getTransactionType()), account);
				account.setBalance(account.getBalance() + dto.getAmount());
				return transactionService.addDepositTransaction(transaction, account);
			}
				
				@GetMapping("/accountholder/{id}/rolloverIRA/{accNum}/transactions")
				@ResponseStatus(HttpStatus.OK)
				@Secured("ROLE_ADMIN")
				public List <Transaction> getTransactionsFromRolloverIRA(@PathVariable int id,@PathVariable long accNum) {
					RolloverIRAAccount account = accountsService.getRolloverIRAAccount(id, accNum);
					return transactionService.getTransactions(account);
			}
				
			@PostMapping("/accountholder/{id}/rolloverIRA/{accNum}/withdrawtransaction")
			@ResponseStatus(HttpStatus.CREATED)
			@Secured("ROLE_ADMIN")
			public Transaction addWithdrawToRolloverIRA(@PathVariable int id,@PathVariable long accNum ,
	  		@RequestBody TransactionDTO dto) throws ZeroAmountException, NegativeAmountException {
				RolloverIRAAccount account = accountsService.getRolloverIRAAccount(id, accNum);
					//double balance = accountsService.getSavingsAccount(id, accNum).getBalance();
				
				if(dto.getAmount() < 0) throw new NegativeAmountException();
				if(account.getBalance() == 0) throw new ZeroAmountException("balance is zero");
				if(account.getBalance() - dto.getAmount() < 0) throw new ZeroAmountException("Invalid Transaction");
				if(dto.getAmount() == 0) throw new ZeroAmountException("Amount cannot be zero");
				
				WithdrawTransaction transaction = new WithdrawTransaction(dto.getAmount(), account.getBalance() + dto.getAmount(), 
				TransactionType.valueOf(dto.getTransactionType()),  accountsService.getRolloverIRAAccount(id, accNum));
				account.setBalance(account.getBalance() + dto.getAmount());
				return transactionService.addWithdrawTransaction(transaction,  account);
			}
			
			
             //transaction endpointsfor Roth IRA Accounts
			
			@PostMapping("/accountholder/{id}/rothIRA/{accNum}/deposittransaction")
			@ResponseStatus(HttpStatus.CREATED)
			@Secured("ROLE_ADMIN")
			public Transaction addDepositransactionToRothIRA(@PathVariable int id,@PathVariable long accNum ,
			@RequestBody TransactionDTO dto) throws NegativeAmountException, ZeroAmountException {
				
				RothIRAAccount account = accountsService.getRothIRAAccount(id, accNum);
				
				if(dto.getAmount() < 0) throw new NegativeAmountException();
				if(account.getBalance() == 0) throw new ZeroAmountException("balance is zero");

				
				DepositTransaction transaction =  new DepositTransaction(dto.getAmount(), 
				account.getBalance() + dto.getAmount(), TransactionType.valueOf(dto.getTransactionType()), account);
				account.setBalance(account.getBalance() + dto.getAmount());
				return transactionService.addDepositTransaction(transaction, account);
			}
				
				@GetMapping("/accountholder/{id}/rothIRA/{accNum}/transactions")
				@ResponseStatus(HttpStatus.OK)
				@Secured("ROLE_ADMIN")
				public List <Transaction> getTransactionsFromRothIRA(@PathVariable int id,@PathVariable long accNum) {
					RothIRAAccount account = accountsService.getRothIRAAccount(id, accNum);
					return transactionService.getTransactions(account);
			}
				
		
			@PostMapping("/accountholder/{id}/rothIRA/{accNum}/withdrawtransaction")
			@ResponseStatus(HttpStatus.CREATED)
			@Secured("ROLE_ADMIN")
			public Transaction addWithdrawToRothIRA(@PathVariable int id,@PathVariable long accNum ,
	  		@RequestBody TransactionDTO dto) throws NegativeAmountException, ZeroAmountException {
				RothIRAAccount account = accountsService.getRothIRAAccount(id, accNum);
					//double balance = accountsService.getSavingsAccount(id, accNum).getBalance();
				
				if(dto.getAmount() < 0) throw new NegativeAmountException();
				if(account.getBalance() == 0) throw new ZeroAmountException("balance is zero");
				if(account.getBalance() - dto.getAmount() < 0) throw new ZeroAmountException("Invalid Transaction");
				if(dto.getAmount() == 0) throw new ZeroAmountException("Amount cannot be zero");
				
				WithdrawTransaction transaction = new WithdrawTransaction(dto.getAmount(), account.getBalance() + dto.getAmount(), 
				TransactionType.valueOf(dto.getTransactionType()),  accountsService.getRothIRAAccount(id, accNum));
				account.setBalance(account.getBalance() + dto.getAmount());
				return transactionService.addWithdrawTransaction(transaction,  account);
		  }
			
}

class TransactionDTO{
	
	double amount;
	String transactionType;
	long source;
	long target;
	
	public TransactionDTO() {}
	
	public long getSource() {
		return source;
	}


	public void setSource(long source) {
		this.source = source;
	}


	public long getTarget() {
		return target;
	}


	public void setTarget(long target) {
		this.target = target;
	}
	
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
}