package com.epam.esm.service.exception;

public class NotUniqueParameterServiceException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public NotUniqueParameterServiceException() {
	}

	public NotUniqueParameterServiceException(String message) {
		super(message);
	}

	public NotUniqueParameterServiceException(Throwable cause) {
		super(cause);
	}

	public NotUniqueParameterServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotUniqueParameterServiceException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
