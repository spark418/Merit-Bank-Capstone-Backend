package com.meritamerica.assignment7.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ZeroBalanceException extends Exception {
	
	public ZeroBalanceException(String message) {
		super(message);
	}

}
