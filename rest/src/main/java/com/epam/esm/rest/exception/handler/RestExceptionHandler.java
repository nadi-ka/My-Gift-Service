package com.epam.esm.rest.exception.handler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.epam.esm.rest.exception.InvalidRequestParametersException;
import com.epam.esm.rest.exception.JsonPatchProcessingException;
import com.epam.esm.rest.exception.NotFoundException;
import com.epam.esm.rest.messagekey.MessageKeyHolder;
import com.epam.esm.service.exception.NotUniqueEmailException;
import com.epam.esm.service.exception.NotUniqueLoginException;
import com.epam.esm.service.exception.ServiceValidationException;

@ControllerAdvice
public class RestExceptionHandler {

	private MessageSource messageSource;

	@Autowired
	public RestExceptionHandler(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException exc) {
		ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), exc.getMessage(),
				System.currentTimeMillis());
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleException(Exception exc) {
		ErrorResponse error = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), exc.getMessage(),
				System.currentTimeMillis());

		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exc) {
		List<FieldError> fieldErrors = exc.getBindingResult().getFieldErrors();
		String message = null;
		String emptyString = " ";
		for (FieldError fieldError: fieldErrors) {
			message = fieldError.getField() + emptyString + fieldError.getDefaultMessage();
		}
		ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), message,
				System.currentTimeMillis());
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
			MethodArgumentTypeMismatchException exc) {
		ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), exc.getMessage(),
				System.currentTimeMillis());
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(
			HttpRequestMethodNotSupportedException exc) {
		ErrorResponse error = new ErrorResponse(HttpStatus.METHOD_NOT_ALLOWED.value(), exc.getMessage(),
				System.currentTimeMillis());
		return new ResponseEntity<>(error, HttpStatus.METHOD_NOT_ALLOWED);
	}

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleInvalidRequestParametersException(
			InvalidRequestParametersException exc) {
		ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), exc.getMessage(),
				System.currentTimeMillis());
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleServiceValidationException(ServiceValidationException exc) {
		ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), exc.getMessage(),
				System.currentTimeMillis());
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleJsonPatchProcessingException(JsonPatchProcessingException exc) {
		ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), exc.getMessage(),
				System.currentTimeMillis());
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException exc) {
		ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), exc.getMessage(),
				System.currentTimeMillis());
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleValidationBindException(BindException exc) {
		ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), exc.getMessage(),
				System.currentTimeMillis());
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException exc) {
		ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), exc.getMessage(),
				System.currentTimeMillis());
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleNotUniqueLoginException(
			NotUniqueLoginException exc) {
		ErrorResponse error = new ErrorResponse(HttpStatus.CONFLICT.value(), exc.getMessage(),
				System.currentTimeMillis());
		return new ResponseEntity<>(error, HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleNotUniqueEmailException(
			NotUniqueEmailException exc) {
		ErrorResponse error = new ErrorResponse(HttpStatus.CONFLICT.value(), exc.getMessage(),
				System.currentTimeMillis());
		return new ResponseEntity<>(error, HttpStatus.CONFLICT);
	}

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException exc) {
		ErrorResponse error = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), exc.getMessage(),
				System.currentTimeMillis());
		return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException exc) {
		ErrorResponse error = new ErrorResponse(HttpStatus.FORBIDDEN.value(),
				messageSource.getMessage((MessageKeyHolder.USER_ACCESS_DENIED), null, LocaleContextHolder.getLocale()),
				System.currentTimeMillis());
		return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
	}

}
