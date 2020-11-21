package com.epam.esm.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class GiftCertificateUpdateDTO extends GiftCertificateCreateDTO {
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Europe/Minsk")
	private LocalDateTime creationDate;

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}

	@Override
	public String toString() {
		return "GiftCertificateUpdateDTO [creationDate=" + creationDate + ", getId()=" + getId() + ", getName()="
				+ getName() + ", getDescription()=" + getDescription() + ", getPrice()=" + getPrice()
				+ ", getDuration()=" + getDuration() + ", getTags()=" + getTags() + "]";
	}

}
