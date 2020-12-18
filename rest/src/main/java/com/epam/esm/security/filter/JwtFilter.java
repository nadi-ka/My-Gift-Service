package com.epam.esm.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtFilter extends OncePerRequestFilter {

	private static final String AUTHORIZATION = "Authorization";
	private static final String BEARER = "Bearer ";
	private static final String EMPTY_STRING = "";

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {


	}

	private String getTokenFromRequest(HttpServletRequest request) {
		String token = request.getHeader(AUTHORIZATION);
		if (token != null && token.startsWith(BEARER)) {
			return token.substring(BEARER.length());
		}
		return EMPTY_STRING;
	}

}
