package com.epam.esm.dto;

import javax.validation.constraints.NotBlank;

public class UserLoginPasswordDTO {

	@NotBlank
	private String login;
	@NotBlank
	private String password;
	
	public UserLoginPasswordDTO() {}

	public UserLoginPasswordDTO(@NotBlank String login, @NotBlank String password) {
		this.login = login;
		this.password = password;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
