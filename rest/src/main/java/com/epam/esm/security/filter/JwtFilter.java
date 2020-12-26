package com.epam.esm.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.epam.esm.security.JwtProvider;

import io.jsonwebtoken.Claims;

@Component
public class JwtFilter extends OncePerRequestFilter {

	private UserDetailsService userDetailsService;
	private JwtProvider jwtProvider;
	private static final String AUTHORIZATION = "Authorization";
	private static final String BEARER = "Bearer ";
	private static final String EMPTY_STRING = "";
	
	private static final Logger LOG = LogManager.getLogger(JwtFilter.class);

	public JwtFilter(UserDetailsService userDetailsService, JwtProvider jwtProvider) {
		this.userDetailsService = userDetailsService;
		this.jwtProvider = jwtProvider;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		Claims claims = null;
		String token = getTokenFromRequest(request);
		LOG.info(">>>>>>>>>>>>>>>>>Filter: " + token);
		if (!token.isEmpty()) {
			claims = jwtProvider.decodeToken(token);
		}
		if (claims != null) {
			String username = claims.getSubject();
			UserDetails userDetails = userDetailsService.loadUserByUsername(username);
			UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
					userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(auth);
		}
		filterChain.doFilter(request, response);
	}

	private String getTokenFromRequest(HttpServletRequest request) {
		String token = request.getHeader(AUTHORIZATION);
		if (token != null && token.startsWith(BEARER)) {
			return token.substring(BEARER.length());
		}
		return EMPTY_STRING;
	}

}
