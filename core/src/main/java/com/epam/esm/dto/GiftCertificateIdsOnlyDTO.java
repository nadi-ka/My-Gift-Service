package com.epam.esm.dto;

import javax.validation.constraints.NotNull;

public class GiftCertificateIdsOnlyDTO {
	
	@NotNull
	private Long id;
	
	public GiftCertificateIdsOnlyDTO() {}
	
	public GiftCertificateIdsOnlyDTO(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
