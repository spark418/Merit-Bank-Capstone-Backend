package com.meritamerica.assignment7.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.meritamerica.assignment7.exceptions.ExceedsCombinedBalanceLimitException;
import com.meritamerica.assignment7.exceptions.ExceedsNumberOfAccountsLimitException;
import com.meritamerica.assignment7.exceptions.NegativeAmountException;
import com.meritamerica.assignment7.exceptions.NoResourceFoundException;
import com.meritamerica.assignment7.models.CDAccount;
import com.meritamerica.assignment7.models.CDAccountDTO;
import com.meritamerica.assignment7.models.CheckingAccount;
import com.meritamerica.assignment7.models.DBACheckingAccount;
import com.meritamerica.assignment7.models.RegularIRAAccount;
import com.meritamerica.assignment7.models.RolloverIRAAccount;
import com.meritamerica.assignment7.models.RothIRAAccount;
import com.meritamerica.assignment7.models.SavingsAccount;
import com.meritamerica.assignment7.services.AccountsService;

@RestController
@CrossOrigin(origins = "*")
public class AccountsController {
	Logger logs = LoggerFactory.getLogger(AccountHolderController.class);
	
	@Autowired
	AccountsService accountsService;
	
	@PostMapping("/accountholder/{id}/checkingaccounts")
	@ResponseStatus(HttpStatus.CREATED)
	@Secured("ROLE_ADMIN")
	public CheckingAccount addCheckingAccount(@PathVariable int id,  @RequestBody CheckingAccount checkingAccount) throws NoResourceFoundException, NegativeAmountException, ExceedsCombinedBalanceLimitException, ExceedsNumberOfAccountsLimitException {
		return accountsService.addCheckingAccount(id,checkingAccount);
	}
	
	@PostMapping("/accountholder/{id}/savingsaccounts")
	@ResponseStatus(HttpStatus.CREATED)
	@Secured("ROLE_ADMIN")
	public SavingsAccount addSavingsAccount(@PathVariable int id,  @RequestBody SavingsAccount savingsAccount) throws NoResourceFoundException, NegativeAmountException, ExceedsCombinedBalanceLimitException, ExceedsNumberOfAccountsLimitException {
		return accountsService.addSavingsAccount(id,savingsAccount);
	}
	
	@PostMapping("/accountholder/{id}/cdaccounts")
	@ResponseStatus(HttpStatus.CREATED)
	@Secured("ROLE_ADMIN")
	public CDAccount addCDAccount(@PathVariable int id,  @RequestBody CDAccountDTO dto) throws NoResourceFoundException, NegativeAmountException {
		
		return accountsService.addCDAccount(id,dto);
	}
	
	@PostMapping("/accountholder/{id}/dbacheckingaccounts")
	@ResponseStatus(HttpStatus.CREATED)
	@Secured("ROLE_ADMIN")
	public DBACheckingAccount addDBACheckingAccount(@PathVariable int id,  @RequestBody DBACheckingAccount dbaCheckingAccount) throws NoResourceFoundException, NegativeAmountException, ExceedsCombinedBalanceLimitException, ExceedsNumberOfAccountsLimitException {
		return accountsService.addDBACheckingAccount(id,dbaCheckingAccount);
	}
	
	@PostMapping("/accountholder/{id}/rolloveriraaccounts")
	@ResponseStatus(HttpStatus.CREATED)
	@Secured("ROLE_ADMIN")
	public RolloverIRAAccount addRolloverIRAAccount(@PathVariable int id,  @RequestBody RolloverIRAAccount rolloverIRAAccount) throws NoResourceFoundException, NegativeAmountException, ExceedsCombinedBalanceLimitException, ExceedsNumberOfAccountsLimitException {
		return accountsService.addRolloverIRAAccount(id,rolloverIRAAccount);
	}
	
	@PostMapping("/accountholder/{id}/rothiraaccounts")
	@ResponseStatus(HttpStatus.CREATED)
	@Secured("ROLE_ADMIN")
	public RothIRAAccount addRothIRAAccount(@PathVariable int id,  @RequestBody RothIRAAccount rothIRAAccount) throws NoResourceFoundException, NegativeAmountException, ExceedsCombinedBalanceLimitException, ExceedsNumberOfAccountsLimitException {
		return accountsService.addRothIRAAccount(id,rothIRAAccount);
	}
	
	@PostMapping("/accountholder/{id}/regulariraaccounts")
	@ResponseStatus(HttpStatus.CREATED)
	@Secured("ROLE_ADMIN")
	public RegularIRAAccount addRegularIRAAccount(@PathVariable int id,  @RequestBody RegularIRAAccount regularIRAAccount) throws NoResourceFoundException, NegativeAmountException, ExceedsCombinedBalanceLimitException, ExceedsNumberOfAccountsLimitException {
		return accountsService.addRegularIRAAccount(id,regularIRAAccount);
	}
}
