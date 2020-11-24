package com.epam.esm.dto;

import javax.validation.constraints.NotNull;

public class GiftCertificateWithIdDTO {
	
	@NotNull(message = "Field value cannot be null")
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
