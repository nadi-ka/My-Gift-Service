package com.epam.esm.dto;

import java.time.LocalDate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserRegisterDTO {
	
	@NotBlank
	@Size(max = 45)
	private String firstName;
	
	@NotBlank
	@Size(max = 45)
	private String lastName;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
	@NotNull
	@PastOrPresent
	private LocalDate dateOfBirth;
	
	@NotBlank
	@Email
	private String email;
	
	@NotBlank
	@Size(min = 5, max = 20)
	private String login;
	
	@NotBlank
	@Size(min = 5, max = 20)
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;
	
	public UserRegisterDTO() {}

	public UserRegisterDTO(@NotBlank String firstName, @NotBlank String lastName, @PastOrPresent LocalDate dateOfBirth,
			@NotBlank @Email String email, @NotBlank String login, @NotBlank String password) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
		this.email = email;
		this.login = login;
		this.password = password;
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

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	@Override
	public String toString() {
		return "UserRegisterDTO [firstName=" + firstName + ", lastName=" + lastName + ", dateOfBirth="
				+ dateOfBirth + ", email=" + email + ", login=" + login + ", password=" + password + "]";
	}

}
