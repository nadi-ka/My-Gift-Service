package com.epam.esm.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class PurchaseDTO {
	
	private long id;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Europe/Minsk")
	private LocalDateTime creationDate;
	
	private List<GiftCertificateGetDTO> certificates;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}

	public List<GiftCertificateGetDTO> getCertificates() {
		return certificates;
	}

	public void setCertificates(List<GiftCertificateGetDTO> certificates) {
		this.certificates = certificates;
	}

	@Override
	public String toString() {
		return "PurchaseDTO [id=" + id + ", creationDate=" + creationDate + "]";
	}


}
