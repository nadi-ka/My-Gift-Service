package com.epam.esm.dto;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

public class UserWithJwt {
	
	private long id;
	private String login;
	private String firstName;
	private String lastName;
    private Collection<? extends GrantedAuthority> grantedAuthorities;
    private String tokenType;
    private String jwt;
    
    public UserWithJwt() {}

	public UserWithJwt(long id, String login, String firstName, String lastName,
			Collection<? extends GrantedAuthority> grantedAuthorities, String tokenType, String jwt) {
		this.id = id;
		this.login = login;
		this.firstName = firstName;
		this.lastName = lastName;
		this.grantedAuthorities = grantedAuthorities;
		this.tokenType = tokenType;
		this.jwt = jwt;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Collection<? extends GrantedAuthority> getGrantedAuthorities() {
		return grantedAuthorities;
	}

	public void setGrantedAuthorities(Collection<? extends GrantedAuthority> grantedAuthorities) {
		this.grantedAuthorities = grantedAuthorities;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public String getJwt() {
		return jwt;
	}

	public void setJwt(String jwt) {
		this.jwt = jwt;
	}
    
}
