package com.epam.esm.service.exception;

public class NotUniqueEmailException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public NotUniqueEmailException() {
	}

	public NotUniqueEmailException(String message) {
		super(message);
	}

	public NotUniqueEmailException(Throwable cause) {
		super(cause);
	}

	public NotUniqueEmailException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotUniqueEmailException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
