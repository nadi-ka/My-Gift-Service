package com.epam.esm.service.exception;

public class IllegalOperationServiceException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public IllegalOperationServiceException() {
	}

	public IllegalOperationServiceException(String message) {
		super(message);
	}

	public IllegalOperationServiceException(Throwable cause) {
		super(cause);
	}

	public IllegalOperationServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public IllegalOperationServiceException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
