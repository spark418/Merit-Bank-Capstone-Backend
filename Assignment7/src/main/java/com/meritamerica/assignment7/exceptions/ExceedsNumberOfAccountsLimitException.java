package com.meritamerica.assignment7.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ExceedsNumberOfAccountsLimitException extends Exception {
	public ExceedsNumberOfAccountsLimitException(String message) {
		super(message);
	}

}
