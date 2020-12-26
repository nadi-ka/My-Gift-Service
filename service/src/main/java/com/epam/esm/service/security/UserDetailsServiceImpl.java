package com.epam.esm.service.security;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.epam.esm.dal.UserDao;
import com.epam.esm.entity.User;
import com.epam.esm.service.exception.EntityNotFoundServiceException;
import com.epam.esm.userdetails.CustomUserDetails;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private UserDao userDao;

	@Autowired
	public UserDetailsServiceImpl(UserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userDao.findUserByLogin(username);
		if (user == null) {
//			throw new EntityNotFoundServiceException("User was not found, login = " + username);
			throw new UsernameNotFoundException("User was not found, login = " + username);
		}
		return convertToUserDetails(user);
	}

	private UserDetails convertToUserDetails(User user) {
		List<GrantedAuthority> authorities = Collections
				.singletonList(new SimpleGrantedAuthority(user.getRole().getValue()));
		return new CustomUserDetails(user.getId(), user.getFirstName(), user.getLastName(), user.getLogin(),
				user.getPassword(), authorities);
	}

}
