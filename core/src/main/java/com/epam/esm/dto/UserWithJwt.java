package com.epam.esm.dto;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

public class UserWithJwt {
	
	private long id;
	private String login;
    private String userName;
    private Collection<? extends GrantedAuthority> grantedAuthorities;
    private String tokenType;
    private String jwt;
    
    public UserWithJwt() {}

	public UserWithJwt(long id, String login, String userName,
			Collection<? extends GrantedAuthority> grantedAuthorities, String tokenType, String jwt) {
		this.id = id;
		this.login = login;
		this.userName = userName;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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
