package com.epam.esm.dto;

import javax.validation.constraints.NotNull;

public class GiftCertificateWithIdDTO {
	
	@NotNull
	private Long id;
	
	public GiftCertificateWithIdDTO() {}
	
	public GiftCertificateWithIdDTO(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
