package com.epam.esm.rest.exception;

public class InvalidRequestParametersException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public InvalidRequestParametersException() {
	}

	public InvalidRequestParametersException(String message) {
		super(message);
	}

	public InvalidRequestParametersException(Throwable cause) {
		super(cause);
	}

	public InvalidRequestParametersException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidRequestParametersException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
