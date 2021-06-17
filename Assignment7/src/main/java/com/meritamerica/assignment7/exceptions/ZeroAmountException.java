package com.meritamerica.assignment7.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class ZeroAmountException extends Exception {
	
	public ZeroAmountException(String message) {
		super(message);
	}

}



