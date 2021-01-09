package com.epam.esm.service.exception;

public class NotUniqueLoginException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public NotUniqueLoginException() {
	}

	public NotUniqueLoginException(String message) {
		super(message);
	}

	public NotUniqueLoginException(Throwable cause) {
		super(cause);
	}

	public NotUniqueLoginException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotUniqueLoginException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
