package com.epam.esm.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserLoginPasswordDTO {

	@NotBlank
	@Size(max = 20)
	private String login;
	@NotBlank
	@Size(max = 20)
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
