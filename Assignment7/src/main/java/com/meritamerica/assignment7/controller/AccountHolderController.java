package com.meritamerica.assignment7.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.meritamerica.assignment7.models.AccountHoldersContactDetails;
import com.meritamerica.assignment7.models.CDAccount;
import com.meritamerica.assignment7.models.CheckingAccount;
import com.meritamerica.assignment7.models.DBACheckingAccount;
import com.meritamerica.assignment7.models.RegularIRAAccount;
import com.meritamerica.assignment7.models.RolloverIRAAccount;
import com.meritamerica.assignment7.models.RothIRAAccount;
import com.meritamerica.assignment7.models.SavingsAccount;

import com.meritamerica.assignment7.security.models.User;
import com.meritamerica.assignment7.exceptions.AccountIsClosedException;
import com.meritamerica.assignment7.exceptions.InvalidAccountDetailsException;
import com.meritamerica.assignment7.exceptions.NoResourceFoundException;
import com.meritamerica.assignment7.models.AccountHolder;
import com.meritamerica.assignment7.services.AccountHolderService;
import com.meritamerica.assignment7.services.AccountsService;
import com.meritamerica.assignment7.services.ClosingAccountService;

@RestController
@CrossOrigin(origins = "*")
public class AccountHolderController {
	Logger logs = LoggerFactory.getLogger(AccountHolderController.class);
	
	@Autowired
	private AccountHolderService accountHolderService;
	
	@Autowired
	private ClosingAccountService closingAccountService;
	
	@Autowired
	private AccountsService accountsService;
	
	
	@PostMapping(value="/accountholder")
	@Secured("ROLE_ADMIN")
	@ResponseStatus(HttpStatus.CREATED)
	public AccountHolder addAccountHolder(@RequestBody @Valid AccountHolder accountHolder) {
		try {
			logs.info("In AccountHolderController.addAccountHolder");
			//accountHolder.addSavingsAccount(100);
			//accountHolder.addCheckingAccount(100);
			return accountHolderService.addAccountHolder(accountHolder);
		} catch (InvalidAccountDetailsException e) {
			// TODO Auto-generated catch block
			logs.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	@PutMapping(value="/accountholder/{id}/update")
	@Secured("ROLE_ADMIN")
	@ResponseStatus(HttpStatus.CREATED)
	public AccountHolder updateAccountHolder(@RequestBody @Valid AccountHolder accountHolder,@PathVariable int id) throws NoResourceFoundException {
		try {
			logs.info("In AccountHolderController.addAccountHolder");
			return accountHolderService.updateAccountHolder(accountHolder,id);
		} catch (NoResourceFoundException e) {
			
			logs.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	@GetMapping(value="/accountholders")
	@Secured("ROLE_ADMIN")
	public List<AccountHolder> getAccHolders() {
		return accountHolderService.getAccountHolders();
	}
	
	@GetMapping(value="/accountholder/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	@Secured("ROLE_ADMIN")
	public AccountHolder getAccHoldersById(@PathVariable int id) throws NoResourceFoundException {
		return accountHolderService.getAccountHolderById(id); 
	}
	
	@PostMapping("/accountholder/{id}/contactdetails")
	@ResponseStatus(HttpStatus.CREATED)
	@Secured("ROLE_ADMIN")
	public AccountHoldersContactDetails addContactDetails(@RequestBody @Valid AccountHoldersContactDetails accountHolderContact,@PathVariable int id) throws NoResourceFoundException {
		return accountHolderService.addContactDetails(id, accountHolderContact);
	}
	
	@PutMapping("/accountholder/{id}/contactdetails/update")
	@ResponseStatus(HttpStatus.CREATED)
	@Secured("ROLE_ADMIN")
	public AccountHoldersContactDetails updateContactDetails(@RequestBody @Valid AccountHoldersContactDetails accountHolderContact,@PathVariable int id) throws NoResourceFoundException {
		return accountHolderService.updateContactDetails(accountHolderContact,id);
	}
	
	@GetMapping("/accountholder/{id}/checkingaccounts")
	@ResponseStatus(HttpStatus.CREATED)
	@Secured("ROLE_ADMIN")
	public List<CheckingAccount> getCheckingAccount(@PathVariable int id) throws NoResourceFoundException{
		return accountHolderService.getCheckingAccount(id);
	}
	
	@GetMapping("/accountholder/{id}/savingsaccounts")
	@ResponseStatus(HttpStatus.CREATED)
	@Secured("ROLE_ADMIN")
	public List<SavingsAccount> getSavingsAccount(@PathVariable int id) throws NoResourceFoundException{
		return accountHolderService.getSavingsAccount(id);
	}
	
	@GetMapping("/accountholder/{id}/cdaccounts")
	@ResponseStatus(HttpStatus.CREATED)
	@Secured("ROLE_ADMIN")
	public List<CDAccount> getCDAccount(@PathVariable int id) throws NoResourceFoundException{
		return accountHolderService.getCDAccount(id);
	}
	
	@GetMapping("/accountholder/{id}/dbacheckingaccounts")
	@ResponseStatus(HttpStatus.CREATED)
	@Secured("ROLE_ADMIN")
	public List<DBACheckingAccount> getDBACheckingAccount(@PathVariable int id) throws NoResourceFoundException{
		return accountHolderService.getDBACheckingAccount(id);
	}
	
	@GetMapping("/accountholder/{id}/regulariraaccounts")
	@ResponseStatus(HttpStatus.CREATED)
	@Secured("ROLE_ADMIN")
	public List<RegularIRAAccount> getRegularIRACheckingAccount(@PathVariable int id) throws NoResourceFoundException{
		return accountHolderService.getRegularIRACheckingAccount(id);
	}
	
	@GetMapping("/accountholder/{id}/rothiraaccounts")
	@ResponseStatus(HttpStatus.CREATED)
	@Secured("ROLE_ADMIN")
	public List<RothIRAAccount> getRothIRAAccount(@PathVariable int id) throws NoResourceFoundException{
		return accountHolderService.getRothIRAAccount(id);
	}
	
	@GetMapping("/accountholder/{id}/rolloveriraaccounts")
	@ResponseStatus(HttpStatus.CREATED)
	@Secured("ROLE_ADMIN")
	public List<RolloverIRAAccount> getRolloverIRAAccount(@PathVariable int id) throws NoResourceFoundException{
		return accountHolderService.getRolloverIRAAccount(id);
	}
	
	@PutMapping("/accountholder/{id}/CloseCheckingAccount")
	@Secured("ROLE_ADMIN")
	public String closeCheckingAccount(@PathVariable int id) throws NoResourceFoundException, 
	AccountIsClosedException {
		if(!accountHolderService.getCheckingAccount(id).get(0).isOpen()) 
			throw new AccountIsClosedException("Account is Closed");
		
		return closingAccountService.closeCheckingAccount(id);
	}
	@PutMapping("/accountholder/{id}/CloseDBAAccount/{accountNum}")
	@Secured("ROLE_ADMIN")
	public String closeDBACheckingAccount(@PathVariable int id, @PathVariable long accountNum) 
			throws NoResourceFoundException, AccountIsClosedException {
		if(!accountsService.getDBACheckingAccount(id, accountNum).isOpen()) 
			throw new AccountIsClosedException("Account is Closed");
		
		return closingAccountService.closeDBAAccount(id, accountNum);
		
	}
	
	@PutMapping("/accountholder/{id}/CloseCDAccount/{accountNum}")
	@Secured("ROLE_ADMIN")
	public String closeCDAccount(@PathVariable int id, @PathVariable long accountNum) throws NoResourceFoundException, 
	AccountIsClosedException {
		//int id = user.getAccountHolder().getId();
		if(!accountsService.getCDAccount(id, accountNum).isOpen()) 
			throw new AccountIsClosedException("Account is Closed");
		return closingAccountService.closeCDAccount(id, accountNum);
		
	}
	
	
	@PutMapping("/accountholder/{id}/CloseRegularIRAAccount")
	@Secured("ROLE_ADMIN")
	public String closeRegularIRAAccount(@PathVariable int id) throws NoResourceFoundException, AccountIsClosedException {

		//int id = user.getAccountHolder().getId();
		if(!accountHolderService.getRegularIRACheckingAccount(id).get(0).isOpen())
			throw new AccountIsClosedException("Account is Closed");
		return closingAccountService.closeRegularIRAAccount(id);
		
	}
	
	@PutMapping("/accountholder/{id}/CloseRolloverIRAAccount")
	@Secured("ROLE_ADMIN")
	public String closeRolloverIRAAccount(@PathVariable int id) throws NoResourceFoundException, AccountIsClosedException {

		//int id = user.getAccountHolder().getId();
		if(!accountHolderService.getRolloverIRAAccount(id).get(0).isOpen()) 
			throw new AccountIsClosedException("Account is Closed");
		return closingAccountService.closeRolloverIRAAccount(id);
	}
	
	@PutMapping("/accountholder/{id}/CloseRothIRAAccount")
	@Secured("ROLE_ADMIN")
	public String closeRothIRAAccount(@PathVariable int id) throws NoResourceFoundException, AccountIsClosedException {

		//int id = user.getAccountHolder().getId();
		if(!accountHolderService.getRothIRAAccount(id).get(0).isOpen()) 
			throw new AccountIsClosedException("Account is Closed");
		return closingAccountService.closeRothIRAAccount(id);
	}

	
	
}
