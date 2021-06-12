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

import com.meritamerica.assignment7.exceptions.InvalidAccountDetailsException;
import com.meritamerica.assignment7.models.CDOffering;
import com.meritamerica.assignment7.services.CDOfferingService;


@RestController
@CrossOrigin(origins = "*")
public class CDOfferingController {
	Logger logs = LoggerFactory.getLogger(AccountHolderController.class);
	@Autowired
	CDOfferingService cdOfferingService;
	
	@PostMapping(value="/cdofferings")
	@ResponseStatus(HttpStatus.CREATED)
	@Secured("ROLE_ADMIN")
	public CDOffering cdOfferings(@RequestBody CDOffering cdoffering) throws InvalidAccountDetailsException {
		return cdOfferingService.addCDOffering(cdoffering);
	}
	
	@GetMapping(value="/cdofferings")
	public List<CDOffering> getCDOfferings() {
		return cdOfferingService.getCDOfferings(); 
	}
	
	@GetMapping(value="/bestcdofferings/{balance}")
	@ResponseStatus(HttpStatus.CREATED)
	@Secured("ROLE_ADMIN")
	public CDOffering getBestCDOffering(@PathVariable double balance) throws InvalidAccountDetailsException {
		return cdOfferingService.getBestCDOffering(balance);
	}
	
	
}
