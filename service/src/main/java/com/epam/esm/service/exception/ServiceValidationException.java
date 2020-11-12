package com.epam.esm.service.exception;

public class ServiceValidationException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public ServiceValidationException() {
	}

	public ServiceValidationException(String message) {
		super(message);
	}

	public ServiceValidationException(Throwable cause) {
		super(cause);
	}

	public ServiceValidationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServiceValidationException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
