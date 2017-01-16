package com.victorlaerte.supermarket;

/**
 * Created by victoroliveira on 15/01/17.
 */

public class SignUpException extends Exception {

	public SignUpException() {
		super();
	}

	public SignUpException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public SignUpException(String message) {
		super(message);
	}

	public SignUpException(Throwable throwable) {
		super(throwable);
	}
}
