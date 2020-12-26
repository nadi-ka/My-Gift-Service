package com.epam.esm.rest.exception.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.epam.esm.rest.messagekey.MessageKeyHolder;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	private MessageSource messageSource;
	private ObjectMapper objectMapper;
	private static final String CONTENT_TYPE = "application/json";
    private static final String ENCODING = "UTF-8";
    
    private static final Logger LOG = LogManager.getLogger(CustomAccessDeniedHandler.class);

	@Autowired
	public CustomAccessDeniedHandler(MessageSource messageSource, ObjectMapper objectMapper) {
		this.messageSource = messageSource;
		this.objectMapper = objectMapper;
	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		ErrorResponse error = new ErrorResponse(HttpStatus.FORBIDDEN.value(), messageSource.getMessage((MessageKeyHolder.USER_ACCESS_DENIED),
				null, RequestContextUtils.getLocale(request)),
				System.currentTimeMillis());
		LOG.info("((((((((((((((CustomAccesDeniedHandler");
		LOG.info(messageSource.getMessage((MessageKeyHolder.USER_ACCESS_DENIED),
				null, RequestContextUtils.getLocale(request)),
				System.currentTimeMillis());
		LOG.info("LOcale: " + LocaleContextHolder.getLocale());
		sendJsonResponse(error, response, HttpStatus.FORBIDDEN.value());
	}
	
	private void sendJsonResponse(ErrorResponse error, HttpServletResponse response, int status) throws IOException {
        response.setStatus(status);
        response.setContentType(CONTENT_TYPE);
        response.setCharacterEncoding(ENCODING);
        response.getWriter().write(objectMapper.writeValueAsString(error));
    }

}
