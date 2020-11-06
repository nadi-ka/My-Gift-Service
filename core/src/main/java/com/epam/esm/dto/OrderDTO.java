package com.epam.esm.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.epam.esm.entity.GiftCertificate;
import com.fasterxml.jackson.annotation.JsonFormat;

public class OrderDTO {
	
	private long id;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Europe/Minsk")
	private LocalDateTime creationDate;
	
	private List<GiftCertificate> certificates;

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

	public List<GiftCertificate> getCertificates() {
		return certificates;
	}

	public void setCertificates(List<GiftCertificate> certificates) {
		this.certificates = certificates;
	}

	@Override
	public String toString() {
		return "OrderDTO [id=" + id + ", creationDate=" + creationDate + ", certificates=" + certificates + "]";
	}

}
