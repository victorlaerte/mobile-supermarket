package com.victorlaerte.supermarket;

/**
 * Created by victoroliveira on 15/01/17.
 */

public class LoginException extends Exception {

	public LoginException() {
		super();
	}

	public LoginException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public LoginException(String message) {
		super(message);
	}

	public LoginException(Throwable throwable) {
		super(throwable);
	}
}
