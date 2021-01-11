package com.epam.esm.rest.exception;

public class JsonPatchProcessingException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public JsonPatchProcessingException() {
	}

	public JsonPatchProcessingException(String message) {
		super(message);
	}

	public JsonPatchProcessingException(Throwable cause) {
		super(cause);
	}

	public JsonPatchProcessingException(String message, Throwable cause) {
		super(message, cause);
	}

	public JsonPatchProcessingException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
