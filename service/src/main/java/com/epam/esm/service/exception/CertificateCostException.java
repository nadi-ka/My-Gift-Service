package com.epam.esm.service.exception;

public class CertificateCostException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public CertificateCostException() {
	}

	public CertificateCostException(String message) {
		super(message);
	}

	public CertificateCostException(Throwable cause) {
		super(cause);
	}

	public CertificateCostException(String message, Throwable cause) {
		super(message, cause);
	}

	public CertificateCostException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
