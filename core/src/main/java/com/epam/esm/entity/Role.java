package com.epam.esm.entity;

public enum Role {

	GUEST("GUEST"), USER("USER"), ADMINISTRATOR("ADMINISTRATOR");

	private String value;

	private Role(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
