package com.epam.esm.dto;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;

public class GiftCertificateUpdateDTO extends GiftCertificateCreateDTO {

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Europe/Minsk")
	private LocalDateTime creationDate;

	public GiftCertificateUpdateDTO() {
		super();
	}

	public GiftCertificateUpdateDTO(long id, @NotBlank @Size(max = 45) String name,
			@NotBlank @Size(max = 100) String description,
			@NotNull @DecimalMin("0.1") @DecimalMax("10000.0") Double price,
			@NotNull @Min(value = 1) @Max(value = 1000) Integer duration, List<TagDTO> tags,
			LocalDateTime creationDate) {
		super(id, name, description, price, duration, tags);
		this.creationDate = creationDate;
	}

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
