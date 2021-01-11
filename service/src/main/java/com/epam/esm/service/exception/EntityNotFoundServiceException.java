package com.epam.esm.service.exception;

public class EntityNotFoundServiceException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public EntityNotFoundServiceException() {
	}

	public EntityNotFoundServiceException(String message) {
		super(message);
	}

	public EntityNotFoundServiceException(Throwable cause) {
		super(cause);
	}

	public EntityNotFoundServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public EntityNotFoundServiceException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
