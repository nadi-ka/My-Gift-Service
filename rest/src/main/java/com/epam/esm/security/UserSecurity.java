package com.epam.esm.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.epam.esm.userdetails.CustomUserDetails;

@Component
public class UserSecurity {

	private UserDetailsService userDetailsService;

	@Autowired
	public UserSecurity(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	public boolean hasUserId(Authentication authentication, long userId) {
		CustomUserDetails userDetails = (CustomUserDetails) userDetailsService
				.loadUserByUsername(authentication.getName());
		return userDetails.getId() == userId;
	}

}
