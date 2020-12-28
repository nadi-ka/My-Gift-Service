package com.epam.esm.rest.exception.handler;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.epam.esm.rest.messagekey.MessageKeyHolder;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class CustomSecurityEntryPoint implements AuthenticationEntryPoint {

	private MessageSource messageSource;
	private ObjectMapper objectMapper;
	private static final String CONTENT_TYPE = "application/json";
	private static final String ENCODING = "UTF-8";
	private static final String LANG = "lang";
	private static final String EN = "en";

	@Autowired
	public CustomSecurityEntryPoint(MessageSource messageSource, ObjectMapper objectMapper) {
		this.messageSource = messageSource;
		this.objectMapper = objectMapper;
	}

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		String lang = request.getParameter(LANG);
		Locale locale = (lang != null) ? new Locale(lang) : new Locale(EN);
		String message = messageSource.getMessage((MessageKeyHolder.USER_AUTH_FAILED), null,
				locale);
		ErrorResponse error = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), message, System.currentTimeMillis());
		sendJsonResponse(error, response, HttpStatus.UNAUTHORIZED.value());
	}

	private void sendJsonResponse(ErrorResponse error, HttpServletResponse response, int status) throws IOException {
		response.setStatus(status);
		response.setContentType(CONTENT_TYPE);
		response.setCharacterEncoding(ENCODING);
		response.getWriter().write(objectMapper.writeValueAsString(error));
	}

}
