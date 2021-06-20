package com.meritamerica.assignment7.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class AccountIsClosedException extends Exception {
	public AccountIsClosedException(String msg) {
		super(msg);
	}
}
