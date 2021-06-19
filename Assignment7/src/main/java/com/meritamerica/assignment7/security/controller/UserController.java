package com.meritamerica.assignment7.security.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.meritamerica.assignment7.enumerations.TransactionType;
import com.meritamerica.assignment7.exceptions.AccountIsClosedException;
import com.meritamerica.assignment7.exceptions.ExceedsCombinedBalanceLimitException;
import com.meritamerica.assignment7.exceptions.ExceedsNumberOfAccountsLimitException;
import com.meritamerica.assignment7.exceptions.NegativeAmountException;
import com.meritamerica.assignment7.exceptions.NoResourceFoundException;
import com.meritamerica.assignment7.exceptions.ZeroAmountException;
import com.meritamerica.assignment7.exceptions.ZeroBalanceException;
import com.meritamerica.assignment7.models.AccountHolder;
import com.meritamerica.assignment7.models.BankAccount;
import com.meritamerica.assignment7.models.CDAccount;
import com.meritamerica.assignment7.models.CDAccountDTO;
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
import com.meritamerica.assignment7.security.AuthenticationRequest;
import com.meritamerica.assignment7.security.AuthenticationResponse;
import com.meritamerica.assignment7.security.models.User;
import com.meritamerica.assignment7.security.services.UserService;
import com.meritamerica.assignment7.security.services.UserServiceImpl;
import com.meritamerica.assignment7.security.util.JwtUtil;
import com.meritamerica.assignment7.services.AccountsService;
import com.meritamerica.assignment7.services.ClosingAccountService;
import com.meritamerica.assignment7.services.TransactionService;

//import jdk.javadoc.internal.doclets.toolkit.resources.doclets;

@RestController
@CrossOrigin(origins = "*")
public class UserController {

	@Autowired
	private JwtUtil jwtTokenUtil;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AccountsService accountsService;
	
	@Autowired 
	private TransactionService transactionService;
	
	@Autowired
	private ClosingAccountService closingAccountService;
	
	
	
	public String closeCheckingAccount() throws NoResourceFoundException {
		String username = jwtTokenUtil.getCurrentUserName();
		User user = userService.getUserByUserName(username);
		
		return closingAccountService.closeCheckingAccount(user.getAccountHolder().getId(), user.getAccountHolder().getCheckingAccountList().get(0).getAccountNumber());
	}

	@GetMapping("/Me")
	@Secured("ROLE_USER")
	public AccountHolder getAccountHolderById() {
		String username = jwtTokenUtil.getCurrentUserName();
		System.out.println("username: "+username);
		User user = userService.getUserByUserName(username);
		return user.getAccountHolder();
	}
	
	@PostMapping("/Me/checkingaccounts")
	@Secured("ROLE_USER")
	public CheckingAccount addCheckingAccount(@RequestBody CheckingAccount checkingAccount) throws NoResourceFoundException, NegativeAmountException, ExceedsCombinedBalanceLimitException, ExceedsNumberOfAccountsLimitException {
		if(checkingAccount.getBalance()<0) {
			throw new NegativeAmountException();
		}
		String username = jwtTokenUtil.getCurrentUserName();
		User user = userService.getUserByUserName(username);
		AccountHolder accHolder= user.getAccountHolder();
		if(accHolder==null) {
			throw new NoResourceFoundException("Invalid id");
		}
		if (accHolder.getCombinedBalance()+checkingAccount.getBalance()>250000) {
			throw new ExceedsCombinedBalanceLimitException("exceeds limit of amount 250,000 max");
		}
		
		return accountsService.addCheckingAccount(accHolder.getId(),checkingAccount);	
	}
	
	@GetMapping("/Me/checkingaccounts")
	@Secured("ROLE_USER")
	public List<CheckingAccount> getCheckingAccount() throws AccountIsClosedException{
		String username = jwtTokenUtil.getCurrentUserName();
		User user = userService.getUserByUserName(username);
		if(!user.getAccountHolder().getCheckingAccountList().get(0).isOpen()) throw new AccountIsClosedException("Account is Closed");
		return user.getAccountHolder().getCheckingAccountList();
	}
	
	@PostMapping("/Me/savingsaccounts")
	@Secured("ROLE_USER")
	public SavingsAccount addSavingsAccount(@RequestBody SavingsAccount savingsAccount) throws NoResourceFoundException, NegativeAmountException, ExceedsCombinedBalanceLimitException, ExceedsNumberOfAccountsLimitException {
		if(savingsAccount.getBalance()<0) {
			throw new NegativeAmountException();
		}
		String username = jwtTokenUtil.getCurrentUserName();
		User user = userService.getUserByUserName(username);
		AccountHolder accHolder= user.getAccountHolder();
		if(accHolder==null) {
			throw new NoResourceFoundException("Invalid id");
		}
		if (accHolder.getCombinedBalance()+savingsAccount.getBalance()>250000) {
			throw new ExceedsCombinedBalanceLimitException("exceeds limit of amount 250,000 max");
		}
		return accountsService.addSavingsAccount(accHolder.getId(),savingsAccount);
	}
	
	@GetMapping("/Me/savingsaccounts")
	@Secured("ROLE_USER")
	public List<SavingsAccount> getSavingsAccount(){
		String username = jwtTokenUtil.getCurrentUserName();
		User user = userService.getUserByUserName(username);
		return user.getAccountHolder().getSavingsAccountList();
	}
	
	@PostMapping("/Me/cdaccounts")
	@Secured("ROLE_USER")
	public CDAccount addCDAccount(@RequestBody CDAccountDTO cdAccount) throws NoResourceFoundException, NegativeAmountException {
		if(cdAccount.getBalance()<0) {
			throw new NegativeAmountException();
		}
		String username = jwtTokenUtil.getCurrentUserName();
		User user = userService.getUserByUserName(username);
		AccountHolder accHolder= user.getAccountHolder();
		
		if(accHolder==null) {
			throw new NoResourceFoundException("Invalid id");
		}
		return accountsService.addCDAccount(accHolder.getId(),cdAccount);
	}
	
	@GetMapping("/Me/cdaccounts")
	@Secured("ROLE_USER")
	public List<CDAccount> getCDAccount(){
		String username = jwtTokenUtil.getCurrentUserName();
		User user = userService.getUserByUserName(username);
		return user.getAccountHolder().getCdAccList();
	}
	
	@PostMapping("/Me/dbacheckingaccounts")
	@Secured("ROLE_USER")
	public DBACheckingAccount addDBACheckingAccount(@RequestBody DBACheckingAccount dbaCheckingAccount) throws NoResourceFoundException, NegativeAmountException, ExceedsCombinedBalanceLimitException, ExceedsNumberOfAccountsLimitException {
		if(dbaCheckingAccount.getBalance()<0) {
			throw new NegativeAmountException();
		}
		String username = jwtTokenUtil.getCurrentUserName();
		User user = userService.getUserByUserName(username);
		AccountHolder accHolder= user.getAccountHolder();
		if(accHolder==null) {
			throw new NoResourceFoundException("Invalid id");
		}

		return accountsService.addDBACheckingAccount(accHolder.getId(),dbaCheckingAccount);
	}
	
	@GetMapping("/Me/dbacheckingaccounts")
	@Secured("ROLE_USER")
	public List<DBACheckingAccount> getDBACheckingAccount(){
		String username = jwtTokenUtil.getCurrentUserName();
		User user = userService.getUserByUserName(username);
		return user.getAccountHolder().getDbaCheckingAccountList();
	}
	@PostMapping("/Me/regulariraaccounts")
	@Secured("ROLE_USER")
	public RegularIRAAccount addRegularIRAAccount(@RequestBody RegularIRAAccount regularIRAAccount) throws NoResourceFoundException, NegativeAmountException, ExceedsCombinedBalanceLimitException, ExceedsNumberOfAccountsLimitException {
		if(regularIRAAccount.getBalance()<0) {
			throw new NegativeAmountException();
		}
		String username = jwtTokenUtil.getCurrentUserName();
		User user = userService.getUserByUserName(username);
		AccountHolder accHolder= user.getAccountHolder();
		if(accHolder==null) {
			throw new NoResourceFoundException("Invalid id");
		}

		return accountsService.addRegularIRAAccount(accHolder.getId(),regularIRAAccount);
	}
	
	@GetMapping("/Me/regulariraaccounts")
	@Secured("ROLE_USER")
	public List<RegularIRAAccount> getRegularIRAAccount(){
		String username = jwtTokenUtil.getCurrentUserName();
		User user = userService.getUserByUserName(username);
		return user.getAccountHolder().getRegularIRAAccountList();
	}
	@PostMapping("/Me/rothiraaccounts")
	@Secured("ROLE_USER")
	public RothIRAAccount addRothIRAAccount(@RequestBody RothIRAAccount rothIRAAccount) throws NoResourceFoundException, NegativeAmountException, ExceedsCombinedBalanceLimitException, ExceedsNumberOfAccountsLimitException {
		if(rothIRAAccount.getBalance()<0) {
			throw new NegativeAmountException();
		}
		String username = jwtTokenUtil.getCurrentUserName();
		User user = userService.getUserByUserName(username);
		AccountHolder accHolder= user.getAccountHolder();
		if(accHolder==null) {
			throw new NoResourceFoundException("Invalid id");
		}

		return accountsService.addRothIRAAccount(accHolder.getId(),rothIRAAccount);
	}
	
	@GetMapping("/Me/rothiraaccounts")
	@Secured("ROLE_USER")
	public List<RothIRAAccount> getRothIRAAccount(){
		String username = jwtTokenUtil.getCurrentUserName();
		User user = userService.getUserByUserName(username);
		return user.getAccountHolder().getRothIRAAccountList();
	}
	@PostMapping("/Me/rolloveriraaccounts")
	@Secured("ROLE_USER")
	public RolloverIRAAccount addRolloverIRAAccount(@RequestBody RolloverIRAAccount rolloverIRAAccount) throws NoResourceFoundException, NegativeAmountException, ExceedsCombinedBalanceLimitException, ExceedsNumberOfAccountsLimitException {
		if(rolloverIRAAccount.getBalance()<0) {
			throw new NegativeAmountException();
		}
		String username = jwtTokenUtil.getCurrentUserName();
		User user = userService.getUserByUserName(username);
		AccountHolder accHolder= user.getAccountHolder();
		if(accHolder==null) {
			throw new NoResourceFoundException("Invalid id");
		}

		return accountsService.addRolloverIRAAccount(accHolder.getId(),rolloverIRAAccount);
	}
	
	@GetMapping("/Me/rolloveriraaccounts")
	@Secured("ROLE_USER")
	public List<RolloverIRAAccount> getRolloverIRAAccount(){
		String username = jwtTokenUtil.getCurrentUserName();
		User user = userService.getUserByUserName(username);
		return user.getAccountHolder().getRolloverIRAAccountList();
		
	}
	/*---------------------TRANSACTION END POINTS-----------------------------*/
	
	//Saving account transaction endpoints 
	
	@PostMapping("/Me/savingsaccount/deposittransaction")
	@ResponseStatus(HttpStatus.CREATED)
	@Secured("ROLE_USER")
	public Transaction depositToSavingsAccount(@RequestBody TransactionDTO dto)  throws NoResourceFoundException, 
	NegativeAmountException, ExceedsCombinedBalanceLimitException, ZeroAmountException{
		
		String username = jwtTokenUtil.getCurrentUserName();
		User user = userService.getUserByUserName(username);
		SavingsAccount account = user.getAccountHolder().getSavingsAccountList().get(0);
		//account.setBalance(dto.getAmount() + account.getBalance());
		if(dto.getAmount() < 0) throw new NegativeAmountException();
		if(dto.getAmount() == 0) throw new ZeroAmountException("amount cannot be zero");
		
		
		DepositTransaction transaction =  new DepositTransaction(dto.getAmount(), 
		account.getBalance() + dto.getAmount(), TransactionType.valueOf(dto.getTransactionType()), account);
		account.setBalance(account.getBalance() + dto.getAmount());
		
		return transactionService.addDepositTransaction(transaction, account);
	}
	
	
	@PostMapping("/Me/savingsaccount/withdrawtransaction")
	@ResponseStatus(HttpStatus.CREATED)
	@Secured("ROLE_USER")
	public Transaction withdrawToSavingsAccount(@RequestBody TransactionDTO dto) throws NegativeAmountException, ZeroAmountException, ZeroBalanceException{
		String username = jwtTokenUtil.getCurrentUserName();
		User user = userService.getUserByUserName(username);
		SavingsAccount account = user.getAccountHolder().getSavingsAccountList().get(0);
		//account.setBalance(dto.getAmount() + account.getBalance());
		
		if(account.getBalance() == 0) throw new ZeroBalanceException("balance is zero");
		if(account.getBalance() < dto.getAmount()) throw new ZeroBalanceException("Invalid Transaction");
		if(dto.getAmount() == 0) throw new ZeroAmountException("Invalid Transaction");
		if(dto.getAmount() < 0) throw new NegativeAmountException();
		
		
		WithdrawTransaction transaction =  new WithdrawTransaction(dto.getAmount(), 
		account.getBalance() -+ dto.getAmount(), TransactionType.valueOf(dto.getTransactionType()), account);
		
		account.setBalance(account.getBalance() -+ dto.getAmount());
		
		return transactionService.addWithdrawTransaction(transaction, account);
		
	}
	
	
	
	@PostMapping("/Me/savingsaccount/transfer")
	@ResponseStatus(HttpStatus.CREATED)
	@Secured("ROLE_USER")
	public Transaction TransferFromSavings(@RequestBody TransactionDTO dto) throws NegativeAmountException, ZeroAmountException, ZeroBalanceException{
		
		String username = jwtTokenUtil.getCurrentUserName();
		User user = userService.getUserByUserName(username);
		SavingsAccount account = user.getAccountHolder().getSavingsAccountList().get(0);
		
		BankAccount target = accountsService.findAccount(dto.getTarget(), dto.getTargetId());
		
		if(account.getBalance() == 0) throw new ZeroBalanceException("balance is zero");
		if(account.getBalance() < dto.getAmount()) throw new ZeroBalanceException("Invalid Transaction");
		if(dto.getAmount() == 0) throw new ZeroAmountException("Invalid Transaction");
		if(dto.getAmount() < 0) throw new NegativeAmountException();
		
		TransferTransaction sourceTransaction = new TransferTransaction(dto.getAmount(), target.getBalance() + dto.getAmount(), 
		account.getBalance() + -dto.getAmount(),
		TransactionType.valueOf(dto.getTransactionType()), account, target);
		
		account.setBalance(account.getBalance() + -dto.getAmount());
		target.setBalance(target.getBalance() + dto.getAmount());
		
		return transactionService.addTransferTransaction(sourceTransaction, account, target);
	}
	
	
	@GetMapping("/Me/savingsaccount/transactions")
	@Secured("ROLE_USER")
	public List<Transaction> getSavingsTransactions() throws NoResourceFoundException{
		String username = jwtTokenUtil.getCurrentUserName();
		User user = userService.getUserByUserName(username);
		 SavingsAccount account = null;
		if(user.getAccountHolder().getSavingsAccountList().size() == 0) 
			throw new NoResourceFoundException("Account is Null");
		else
			account = user.getAccountHolder().getSavingsAccountList().get(0);
		    return account.getAllTransactions();
	}
	
	
	//checking account transaction endpoints
	
	@PostMapping("/Me/checkingaccount/deposittransaction")
	@ResponseStatus(HttpStatus.CREATED)
	@Secured("ROLE_USER")
	public Transaction depositToCheckingAccount(@RequestBody TransactionDTO dto) throws ZeroAmountException, NegativeAmountException{
		String username = jwtTokenUtil.getCurrentUserName();
		User user = userService.getUserByUserName(username);
		CheckingAccount account = user.getAccountHolder().getCheckingAccountList().get(0);
		//account.setBalance(dto.getAmount() + account.getBalance());
		
		if(dto.getAmount() < 0) throw new NegativeAmountException();
		if(dto.getAmount() == 0) throw new ZeroAmountException("amount cannot be zero");
		
		
		DepositTransaction transaction =  new DepositTransaction(dto.getAmount(), 
		account.getBalance() + dto.getAmount(), TransactionType.valueOf(dto.getTransactionType()), account);
		account.setBalance(account.getBalance() + dto.getAmount());
		
		return transactionService.addDepositTransaction(transaction, account);
		
	}
	
	
	@PostMapping("/Me/checkingaccount/withdrawtransaction")
	@ResponseStatus(HttpStatus.CREATED)
	@Secured("ROLE_USER")
	public Transaction withdrawToCheckingAccount(@RequestBody TransactionDTO dto) throws NegativeAmountException, 
	ZeroAmountException, ZeroBalanceException{
		String username = jwtTokenUtil.getCurrentUserName();
		User user = userService.getUserByUserName(username);
		CheckingAccount account = user.getAccountHolder().getCheckingAccountList().get(0);
		//account.setBalance(dto.getAmount() + account.getBalance());
		
		if(account.getBalance() == 0) throw new ZeroBalanceException("balance is zero");
		if(account.getBalance() <dto.getAmount() ) throw new ZeroBalanceException("Invalid Transaction");
		if(dto.getAmount() == 0) throw new ZeroAmountException("Invalid Transaction");
		if(dto.getAmount() < 0) throw new NegativeAmountException();

		
		WithdrawTransaction transaction =  new WithdrawTransaction(dto.getAmount(), 
		account.getBalance() -+ dto.getAmount(), TransactionType.valueOf(dto.getTransactionType()), account);
		
		account.setBalance(account.getBalance() -+ dto.getAmount());
		
		
		return transactionService.addWithdrawTransaction(transaction, account);
		
	}
	
	@PostMapping("/Me/checkingaccount/transfer")
	@ResponseStatus(HttpStatus.CREATED)
	@Secured("ROLE_USER")
	public Transaction TransferFromChecking(@RequestBody TransactionDTO dto) throws NegativeAmountException, 
	ZeroAmountException, ZeroBalanceException{
		
		String username = jwtTokenUtil.getCurrentUserName();
		User user = userService.getUserByUserName(username);
		CheckingAccount account = user.getAccountHolder().getCheckingAccountList().get(0);
		
		BankAccount target = accountsService.findAccount(dto.getTarget(), dto.getTargetId());
		
		if(dto.getAmount() < 0) throw new NegativeAmountException();
		if(account.getBalance() == 0) throw new ZeroBalanceException("InvalidTransaction");
		if(account.getBalance() < dto.getAmount()) throw new ZeroBalanceException("Invalid Transaction");
		if(dto.getAmount() == 0) throw new ZeroAmountException("Amount cannot be zero");
		
		
		TransferTransaction sourceTransaction = new TransferTransaction(dto.getAmount(), target.getBalance() + dto.getAmount(), 
		account.getBalance() + -dto.getAmount(),
		TransactionType.valueOf(dto.getTransactionType()), account, target);
		
	    account.setBalance(account.getBalance() + -dto.getAmount());
		target.setBalance(target.getBalance() + dto.getAmount());
		return transactionService.addTransferTransaction(sourceTransaction, account, target);
	}
	
	@GetMapping("/Me/checkingaccount/transactions")
	@Secured("ROLE_USER")
	public List<Transaction> getCheckingTransactions() throws NoResourceFoundException{
		String username = jwtTokenUtil.getCurrentUserName();
		User user = userService.getUserByUserName(username);
		 CheckingAccount account = null;
		if(user.getAccountHolder().getCheckingAccountList().size() == 0) 
			throw new NoResourceFoundException("Account is Null");
		else
			account = user.getAccountHolder().getCheckingAccountList().get(0);
		    return account.getAllTransactions();
	}
	

	
	//dba account endpoints
	  
	@PostMapping("/Me/dbaccount/{accountNum}/deposittransaction")
	@ResponseStatus(HttpStatus.CREATED)
	@Secured("ROLE_USER")
	public Transaction depositToDBAAccount(@RequestBody TransactionDTO dto, @PathVariable long accountNum) throws NegativeAmountException, ZeroAmountException{
		String username = jwtTokenUtil.getCurrentUserName();
		User user = userService.getUserByUserName(username);
		DBACheckingAccount account = accountsService.getDBACheckingAccount(user.getAccountHolder().getId(), accountNum);
		//account.setBalance(dto.getAmount() + account.getBalance());
		
		if(dto.getAmount() < 0) throw new NegativeAmountException();
		if(dto.getAmount()== 0) throw new ZeroAmountException("amount cannot be zero");
		
		
		DepositTransaction transaction =  new DepositTransaction(dto.getAmount(), 
		account.getBalance() + dto.getAmount(), TransactionType.valueOf(dto.getTransactionType()), account);
		account.setBalance(account.getBalance() + dto.getAmount());
		
		return transactionService.addDepositTransaction(transaction, account);
		
	}
	
	@PostMapping("/Me/dbaccount/{accountNum}/withdrawtransaction")
	@ResponseStatus(HttpStatus.CREATED)
	@Secured("ROLE_USER")
	public Transaction withdrawToDBAAccount(@RequestBody TransactionDTO dto,  @PathVariable long accountNum) throws 
	NegativeAmountException, ZeroAmountException{
		String username = jwtTokenUtil.getCurrentUserName();
		User user = userService.getUserByUserName(username);
		DBACheckingAccount account = accountsService.getDBACheckingAccount(user.getAccountHolder().getId(), accountNum);
		//account.setBalance(dto.getAmount() + account.getBalance());
		if(account.getBalance() == 0) throw new ZeroAmountException("balance is zero");
		if(account.getBalance() < dto.getAmount() ) throw new ZeroAmountException("Invalid Transaction");
		if(dto.getAmount() == 0) throw new ZeroAmountException("Amount cannot be zero");
		if(dto.getAmount() < 0) throw new NegativeAmountException();

		
		WithdrawTransaction transaction =  new WithdrawTransaction(dto.getAmount(), 
		account.getBalance() -+ dto.getAmount(), TransactionType.valueOf(dto.getTransactionType()), account);
		
		account.setBalance(account.getBalance() -+ dto.getAmount());
		
		
		return transactionService.addWithdrawTransaction(transaction, account);
		
	}
	
	@PostMapping("/Me/dbaccount/{accountNum}/transfer")
	@ResponseStatus(HttpStatus.CREATED)
	@Secured("ROLE_USER")
	public Transaction TransferFromDBA(@RequestBody TransactionDTO dto,  @PathVariable long accountNum) throws NegativeAmountException, 
	ZeroAmountException, ZeroBalanceException{
		
		String username = jwtTokenUtil.getCurrentUserName();
		User user = userService.getUserByUserName(username);
		DBACheckingAccount account = accountsService.getDBACheckingAccount(user.getAccountHolder().getId(), accountNum);
		
		BankAccount target = accountsService.findAccount(dto.getTarget(), dto.getTargetId());
		
		if(dto.getAmount() < 0) throw new NegativeAmountException();
		if(account.getBalance() == 0) throw new ZeroBalanceException("InvalidTransaction");
		if(account.getBalance() < dto.getAmount() ) throw new ZeroBalanceException("Invalid Transaction");
		if(dto.getAmount() == 0) throw new ZeroAmountException("Amount cannot be zero");
		
		
		TransferTransaction sourceTransaction = new TransferTransaction(dto.getAmount(), target.getBalance() + dto.getAmount(), 
		account.getBalance() + -dto.getAmount(),
		TransactionType.valueOf(dto.getTransactionType()), account, target);
		
	    account.setBalance(account.getBalance() + -dto.getAmount());
		target.setBalance(target.getBalance() + dto.getAmount());
		return transactionService.addTransferTransaction(sourceTransaction, account, target);
	}
	
	
	@GetMapping("/Me/dbaaccount/{accountNum}/transactions")
	@Secured("ROLE_USER")
	public List<Transaction> getDBATransactions(@PathVariable long accountNum) throws NoResourceFoundException{
		String username = jwtTokenUtil.getCurrentUserName();
		User user = userService.getUserByUserName(username);
		DBACheckingAccount account = null;
		if(user.getAccountHolder().getDbaCheckingAccountList().size() == 0) 
			throw new NoResourceFoundException("Vacant DBA Accounts");
		else
			account = user.getAccountHolder().getDbaCheckingAccountList().get(0);
	    account = accountsService.getDBACheckingAccount(user.getAccountHolder().getId(), accountNum);
	    
	    if(account == null) throw new NoResourceFoundException("Invalid Account Number");
	    
	    return account.getAllTransactions();
	}
	

	

	//cd accounts transaction endpoints
	
	@PostMapping("/Me/cdaccount/{accountNum}/deposittransaction")
	@ResponseStatus(HttpStatus.CREATED)
	@Secured("ROLE_USER")
	public Transaction depositToCDAccount(@RequestBody TransactionDTO dto, @PathVariable long accountNum) throws NegativeAmountException, 
	ZeroAmountException{
		String username = jwtTokenUtil.getCurrentUserName();
		User user = userService.getUserByUserName(username);
		CDAccount account = accountsService.getCDAccount(user.getAccountHolder().getId(), accountNum);
		//account.setBalance(dto.getAmount() + account.getBalance());
		
		if(dto.getAmount() < 0) throw new NegativeAmountException();
		if(dto.getAmount()== 0) throw new ZeroAmountException("amount cannot be zero");
		
		
		DepositTransaction transaction =  new DepositTransaction(dto.getAmount(), 
		account.getBalance() + dto.getAmount(), TransactionType.valueOf(dto.getTransactionType()), account);
		account.setBalance(account.getBalance() + dto.getAmount());
		
		return transactionService.addDepositTransaction(transaction, account);
		
	}
	
	@PostMapping("/Me/cdaccount/{accountNum}/withdrawtransaction")
	@ResponseStatus(HttpStatus.CREATED)
	@Secured("ROLE_USER")
	public Transaction withdrawToCDAccount(@RequestBody TransactionDTO dto,  @PathVariable long accountNum) throws NegativeAmountException, ZeroAmountException, ZeroBalanceException{
		String username = jwtTokenUtil.getCurrentUserName();
		User user = userService.getUserByUserName(username);
		CDAccount account = accountsService.getCDAccount(user.getAccountHolder().getId(), accountNum);
		//account.setBalance(dto.getAmount() + account.getBalance());
		
		if(account.getBalance() == 0) throw new ZeroBalanceException("balance is zero");
		if(account.getBalance() < dto.getAmount() ) throw new ZeroBalanceException("Invalid Transaction");
		if(dto.getAmount() == 0) throw new ZeroAmountException("Invalid Transaction");
		if(dto.getAmount() < 0) throw new NegativeAmountException();

		
		WithdrawTransaction transaction =  new WithdrawTransaction(dto.getAmount(), 
		account.getBalance() -+ dto.getAmount(), TransactionType.valueOf(dto.getTransactionType()), account);
		
		account.setBalance(account.getBalance() -+ dto.getAmount());
		
		
		return transactionService.addWithdrawTransaction(transaction, account);
		
	}
	
	@PostMapping("/Me/cdaccount/{accountNum}/transfer")
	@ResponseStatus(HttpStatus.CREATED)
	@Secured("ROLE_USER")
	public Transaction TransferFromCD(@RequestBody TransactionDTO dto,  @PathVariable long accountNum) throws NegativeAmountException, ZeroAmountException, ZeroBalanceException{
		
		String username = jwtTokenUtil.getCurrentUserName();
		User user = userService.getUserByUserName(username);
		CDAccount account = accountsService.getCDAccount(user.getAccountHolder().getId(), accountNum);
		
		BankAccount target = accountsService.findAccount(dto.getTarget(), dto.getTargetId());
		
		if(dto.getAmount() < 0) throw new NegativeAmountException();
		if(account.getBalance() == 0) throw new ZeroBalanceException("InvalidTransaction");
		if(account.getBalance() < dto.getAmount() ) throw new ZeroBalanceException("Invalid Transaction");
		if(dto.getAmount() == 0) throw new ZeroAmountException("Amount cannot be zero");
		
		
		
		TransferTransaction sourceTransaction = new TransferTransaction(dto.getAmount(), target.getBalance() + dto.getAmount(), 
		account.getBalance() + -dto.getAmount(),
		TransactionType.valueOf(dto.getTransactionType()), account, target);
		
	    account.setBalance(account.getBalance() + -dto.getAmount());
		target.setBalance(target.getBalance() + dto.getAmount());
		return transactionService.addTransferTransaction(sourceTransaction, account, target);
	}
	
	
	@GetMapping("/Me/cdaccount/{accountNum}/transactions")
	@Secured("ROLE_USER")
	public List<Transaction> getCDTransactions(@PathVariable long accountNum) throws NoResourceFoundException{
		String username = jwtTokenUtil.getCurrentUserName();
		User user = userService.getUserByUserName(username);
		CDAccount account = null;
		if(user.getAccountHolder().getCdAccList().size() == 0) 
			throw new NoResourceFoundException("Vacant CD Accounts");
		else
			account = user.getAccountHolder().getCdAccList().get(0);
	    account = accountsService.getCDAccount(user.getAccountHolder().getId(), accountNum);
	    
	    if(account == null) throw new NoResourceFoundException("Invalid Account Number");
	    return account.getAllTransactions();
	}
	

	

	
	//Regular IRA Account Transactions endpoints
	
	
	@PostMapping("/Me/RegularIRA/deposittransaction")
	@ResponseStatus(HttpStatus.CREATED)
	@Secured("ROLE_USER")
	public Transaction depositToRegularIRAAccount(@RequestBody TransactionDTO dto) throws NegativeAmountException, ZeroAmountException{
		String username = jwtTokenUtil.getCurrentUserName();
		User user = userService.getUserByUserName(username);
		RegularIRAAccount account = user.getAccountHolder().getRegularIRAAccountList().get(0);
		//account.setBalance(dto.getAmount() + account.getBalance());
		
		if(dto.getAmount() < 0) throw new NegativeAmountException();
		if(dto.getAmount()== 0) throw new ZeroAmountException("amount cannot be zero");
		
		DepositTransaction transaction =  new DepositTransaction(dto.getAmount(), 
		account.getBalance() + dto.getAmount(), TransactionType.valueOf(dto.getTransactionType()), account);
		account.setBalance(account.getBalance() + dto.getAmount());
		
		return transactionService.addDepositTransaction(transaction, account);
		
	}
	
	@PostMapping("/Me/RegularIRA/withdrawtransaction")
	@ResponseStatus(HttpStatus.CREATED)
	@Secured("ROLE_USER")
	public Transaction withdrawToRegularIRAAccount(@RequestBody TransactionDTO dto) throws NegativeAmountException, ZeroAmountException, ZeroBalanceException{
		String username = jwtTokenUtil.getCurrentUserName();
		User user = userService.getUserByUserName(username);
		RegularIRAAccount account = user.getAccountHolder().getRegularIRAAccountList().get(0);
		//account.setBalance(dto.getAmount() + account.getBalance());
		
		if(account.getBalance() == 0) throw new ZeroBalanceException("balance is zero");
		if(account.getBalance() < dto.getAmount() ) throw new ZeroBalanceException("Invalid Transaction");
		if(dto.getAmount() == 0) throw new ZeroAmountException("Invalid Transaction");
		if(dto.getAmount() < 0) throw new NegativeAmountException();
		
		WithdrawTransaction transaction =  new WithdrawTransaction(dto.getAmount(), 
		account.getBalance() -+ dto.getAmount(), TransactionType.valueOf(dto.getTransactionType()), account);
		
		account.setBalance(account.getBalance() -+ dto.getAmount());
		
		return transactionService.addWithdrawTransaction(transaction, account);
		
	}
	
	
	
	@PostMapping("/Me/RegularIRA/transfer")
	@ResponseStatus(HttpStatus.CREATED)
	@Secured("ROLE_USER")
	public Transaction TransferFromRegularIRA(@RequestBody TransactionDTO dto) throws NegativeAmountException, ZeroAmountException, ZeroBalanceException{
		
		String username = jwtTokenUtil.getCurrentUserName();
		User user = userService.getUserByUserName(username);
		RegularIRAAccount account = user.getAccountHolder().getRegularIRAAccountList().get(0);
		
		BankAccount target = accountsService.findAccount(dto.getTarget(), dto.getTargetId());
		
		if(dto.getAmount() < 0) throw new NegativeAmountException();
		if(account.getBalance() == 0) throw new ZeroBalanceException("InvalidTransaction");
		if(account.getBalance() < dto.getAmount() ) throw new ZeroBalanceException("Invalid Transaction");
		if(dto.getAmount() == 0) throw new ZeroAmountException("Amount cannot be zero");
		
		
		TransferTransaction sourceTransaction = new TransferTransaction(dto.getAmount(), target.getBalance() + dto.getAmount(), 
		account.getBalance() + -dto.getAmount(),
		TransactionType.valueOf(dto.getTransactionType()), account, target);
		
		account.setBalance(account.getBalance() + -dto.getAmount());
		target.setBalance(target.getBalance() + dto.getAmount());
		
		return transactionService.addTransferTransaction(sourceTransaction, account, target);
	}
	
	
	
	@GetMapping("/Me/RegularIRA/transactions")
	@Secured("ROLE_USER")
	public List<Transaction> getRegularTransactions() throws NoResourceFoundException{
		String username = jwtTokenUtil.getCurrentUserName();
		User user = userService.getUserByUserName(username);
		 RegularIRAAccount account = null;
		if(user.getAccountHolder().getCheckingAccountList().size() == 0) 
			throw new NoResourceFoundException("Account is Null");
		else
			account = user.getAccountHolder().getRegularIRAAccountList().get(0);
		    return account.getAllTransactions();
	}
	
	
	//Rollover IRA Account Transactions endpoints
	
	@PostMapping("/Me/RolloverIRA/deposittransaction")
	@ResponseStatus(HttpStatus.CREATED)
	@Secured("ROLE_USER")
	public Transaction depositToRolloverIRAAccount(@RequestBody TransactionDTO dto) throws NegativeAmountException, ZeroAmountException{
		String username = jwtTokenUtil.getCurrentUserName();
		User user = userService.getUserByUserName(username);
		RolloverIRAAccount account = user.getAccountHolder().getRolloverIRAAccountList().get(0);
		//account.setBalance(dto.getAmount() + account.getBalance());
		
		if(dto.getAmount() < 0) throw new NegativeAmountException();
		if(dto.getAmount()== 0) throw new ZeroAmountException("amount cannot be zero");
		
		
		DepositTransaction transaction =  new DepositTransaction(dto.getAmount(), 
		account.getBalance() + dto.getAmount(), TransactionType.valueOf(dto.getTransactionType()), account);
		account.setBalance(account.getBalance() + dto.getAmount());
		
		return transactionService.addDepositTransaction(transaction, account);
		
	}
	
	@PostMapping("/Me/RolloverIRA/withdrawtransaction")
	@ResponseStatus(HttpStatus.CREATED)
	@Secured("ROLE_USER")
	public Transaction withdrawToRolloverIRAAccount(@RequestBody TransactionDTO dto) throws NegativeAmountException, 
	ZeroAmountException, ZeroBalanceException{
		String username = jwtTokenUtil.getCurrentUserName();
		User user = userService.getUserByUserName(username);
		RolloverIRAAccount account = user.getAccountHolder().getRolloverIRAAccountList().get(0);
		//account.setBalance(dto.getAmount() + account.getBalance());
		
		if(account.getBalance() == 0) throw new ZeroBalanceException("balance is zero");
		if(account.getBalance() < dto.getAmount()) throw new ZeroBalanceException("Invalid Transaction");
		if(dto.getAmount() == 0) throw new ZeroAmountException("Invalid Transaction");
		if(dto.getAmount() < 0) throw new NegativeAmountException();

		
		WithdrawTransaction transaction =  new WithdrawTransaction(dto.getAmount(), 
		account.getBalance() -+ dto.getAmount(), TransactionType.valueOf(dto.getTransactionType()), account);
		
		account.setBalance(account.getBalance() -+ dto.getAmount());
		
		return transactionService.addWithdrawTransaction(transaction, account);
		
	}
	
	
	
	@PostMapping("/Me/RolloverIRA/transfer")
	@ResponseStatus(HttpStatus.CREATED)
	@Secured("ROLE_USER")
	public Transaction TransferFromRolloverIRA(@RequestBody TransactionDTO dto) throws NegativeAmountException, ZeroAmountException, ZeroBalanceException{
		
		String username = jwtTokenUtil.getCurrentUserName();
		User user = userService.getUserByUserName(username);
		RolloverIRAAccount account = user.getAccountHolder().getRolloverIRAAccountList().get(0);
		
		BankAccount target = accountsService.findAccount(dto.getTarget(), dto.getTargetId());
		
		if(dto.getAmount() < 0) throw new NegativeAmountException();
		if(account.getBalance() == 0) throw new ZeroBalanceException("InvalidTransaction");
		if(account.getBalance() < dto.getAmount() ) throw new ZeroBalanceException("Invalid Transaction");
		if(dto.getAmount() == 0) throw new ZeroAmountException("Amount cannot be zero");
		
		
		TransferTransaction sourceTransaction = new TransferTransaction(dto.getAmount(), target.getBalance() + dto.getAmount(), 
		account.getBalance() + -dto.getAmount(),
		TransactionType.valueOf(dto.getTransactionType()), account, target);
		
		account.setBalance(account.getBalance() + -dto.getAmount());
		target.setBalance(target.getBalance() + dto.getAmount());
		
		return transactionService.addTransferTransaction(sourceTransaction, account, target);
	}
	
	
	@GetMapping("/Me/RolloverIRA/transactions")
	@Secured("ROLE_USER")
	public List<Transaction> getRolloverTransactions() throws NoResourceFoundException{
		String username = jwtTokenUtil.getCurrentUserName();
		User user = userService.getUserByUserName(username);
		 RolloverIRAAccount account = null;
		if(user.getAccountHolder().getCheckingAccountList().size() == 0) 
			throw new NoResourceFoundException("Account is Null");
		else
			account = user.getAccountHolder().getRolloverIRAAccountList().get(0);
		    return account.getAllTransactions();
	}
	
	
	
	//Roth IRA Account Transactions endpoints
	
	
	@PostMapping("/Me/RothIRA/deposittransaction")
	@ResponseStatus(HttpStatus.CREATED)
	@Secured("ROLE_USER")
	public Transaction depositToRothIRAAccount(@RequestBody TransactionDTO dto) throws NegativeAmountException, ZeroAmountException{
		String username = jwtTokenUtil.getCurrentUserName();
		User user = userService.getUserByUserName(username);
		RothIRAAccount account = user.getAccountHolder().getRothIRAAccountList().get(0);
		//account.setBalance(dto.getAmount() + account.getBalance());
		
		if(dto.getAmount() < 0) throw new NegativeAmountException();
		if(dto.getAmount()== 0) throw new ZeroAmountException("amount cannot be zero");
		
		
		
		DepositTransaction transaction =  new DepositTransaction(dto.getAmount(), 
		account.getBalance() + dto.getAmount(), TransactionType.valueOf(dto.getTransactionType()), account);
		account.setBalance(account.getBalance() + dto.getAmount());
		
		return transactionService.addDepositTransaction(transaction, account);
		
	}
	
	
	@PostMapping("/Me/RothIRA/withdrawtransaction")
	@ResponseStatus(HttpStatus.CREATED)
	@Secured("ROLE_USER")
	public Transaction withdrawToRothIRAAccount(@RequestBody TransactionDTO dto) throws NegativeAmountException, ZeroAmountException, ZeroBalanceException{
		String username = jwtTokenUtil.getCurrentUserName();
		User user = userService.getUserByUserName(username);
		RothIRAAccount account = user.getAccountHolder().getRothIRAAccountList().get(0);
		//account.setBalance(dto.getAmount() + account.getBalance());
		
		if(account.getBalance() == 0) throw new ZeroBalanceException("balance is zero");
		if(account.getBalance() < dto.getAmount() ) throw new ZeroBalanceException("Invalid Transaction");
		if(dto.getAmount() == 0) throw new ZeroAmountException("Invalid Transaction");
		if(dto.getAmount() < 0) throw new NegativeAmountException();

		
		WithdrawTransaction transaction =  new WithdrawTransaction(dto.getAmount(), 
		account.getBalance() -+ dto.getAmount(), TransactionType.valueOf(dto.getTransactionType()), account);
		
		account.setBalance(account.getBalance() -+ dto.getAmount());
		
		
		return transactionService.addWithdrawTransaction(transaction, account);
		
	}
	
	
	
	@PostMapping("/Me/RothIRA/transfer")
	@ResponseStatus(HttpStatus.CREATED)
	@Secured("ROLE_USER")
	public Transaction TransferFromRothIRA(@RequestBody TransactionDTO dto) throws NegativeAmountException, ZeroAmountException, ZeroBalanceException{
		
		String username = jwtTokenUtil.getCurrentUserName();
		User user = userService.getUserByUserName(username);
		RothIRAAccount account = user.getAccountHolder().getRothIRAAccountList().get(0);
		
		BankAccount target = accountsService.findAccount(dto.getTarget(), dto.getTargetId());
		
		if(dto.getAmount() < 0) throw new NegativeAmountException();
		if(account.getBalance() == 0) throw new ZeroBalanceException("InvalidTransaction");
		if(account.getBalance() < dto.getAmount() ) throw new ZeroBalanceException("Invalid Transaction");
		if(dto.getAmount() == 0) throw new ZeroAmountException("Amount cannot be zero");
		
		TransferTransaction sourceTransaction = new TransferTransaction(dto.getAmount(), target.getBalance() + dto.getAmount(), 
		account.getBalance() + -dto.getAmount(),
		TransactionType.valueOf(dto.getTransactionType()), account, target);
		
		account.setBalance(account.getBalance() + -dto.getAmount());
		target.setBalance(target.getBalance() + dto.getAmount());
		
		return transactionService.addTransferTransaction(sourceTransaction, account, target);
	}
	
	
	@GetMapping("/Me/RothIRA/transactions")
	@Secured("ROLE_USER")
	public List<Transaction> getRothIRATransactions() throws NoResourceFoundException{
		String username = jwtTokenUtil.getCurrentUserName();
		User user = userService.getUserByUserName(username);
		 RothIRAAccount account = null;
		if(user.getAccountHolder().getCheckingAccountList().size() == 0) 
			throw new NoResourceFoundException("Account is Null");
		else
			account = user.getAccountHolder().getRothIRAAccountList().get(0);
		    return account.getAllTransactions();
	}
	
	
	
	
	
}


class TransactionDTO{
	
	double amount;
	String transactionType;
	long source;
	long target;
	int targetId;
	
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

	public int getTargetId() {
		return targetId;
	}

	public void setTargetId(int targetId) {
		this.targetId = targetId;
	}
	
	
}
