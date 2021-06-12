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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.meritamerica.assignment7.exceptions.ExceedsCombinedBalanceLimitException;
import com.meritamerica.assignment7.exceptions.ExceedsNumberOfAccountsLimitException;
import com.meritamerica.assignment7.exceptions.NegativeAmountException;
import com.meritamerica.assignment7.exceptions.NoResourceFoundException;
import com.meritamerica.assignment7.models.AccountHolder;
import com.meritamerica.assignment7.models.CDAccount;
import com.meritamerica.assignment7.models.CDAccountDTO;
import com.meritamerica.assignment7.models.CheckingAccount;
import com.meritamerica.assignment7.models.DBACheckingAccount;
import com.meritamerica.assignment7.models.RegularIRAAccount;
import com.meritamerica.assignment7.models.RolloverIRAAccount;
import com.meritamerica.assignment7.models.RothIRAAccount;
import com.meritamerica.assignment7.models.SavingsAccount;
import com.meritamerica.assignment7.security.AuthenticationRequest;
import com.meritamerica.assignment7.security.AuthenticationResponse;
import com.meritamerica.assignment7.security.models.User;
import com.meritamerica.assignment7.security.services.UserService;
import com.meritamerica.assignment7.security.services.UserServiceImpl;
import com.meritamerica.assignment7.security.util.JwtUtil;
import com.meritamerica.assignment7.services.AccountsService;

@RestController
@CrossOrigin(origins = "*")
public class UserController {

	@Autowired
	private JwtUtil jwtTokenUtil;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AccountsService accountsService;

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
	public List<CheckingAccount> getCheckingAccount(){
		String username = jwtTokenUtil.getCurrentUserName();
		User user = userService.getUserByUserName(username);
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

}
