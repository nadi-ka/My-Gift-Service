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

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonFormat;

public class GiftCertificateGetDTO extends RepresentationModel<GiftCertificateGetDTO> {

	private long id;

	@NotBlank
	@Size(max = 45, message = "must be less or equal to 45")
	private String name;

	@NotBlank(message = "is required")
	@Size(max = 100, message = "must be less or equal to 100")
	private String description;

	@NotNull(message = "is required")
	@DecimalMin("0.1")
	@DecimalMax("10000.0")
	private Double price;

	@NotNull(message = "is required")
	@Min(value = 1, message = "must be grater or equal to 1")
	@Max(value = 1000, message = "must be less or equal to 1000")
	private Integer duration;

	private List<TagDTO> tags;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Europe/Minsk")
	private LocalDateTime creationDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
	private LocalDateTime lastUpdateDate;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public List<TagDTO> getTags() {
		return tags;
	}

	public void setTags(List<TagDTO> tags) {
		this.tags = tags;
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}

	public LocalDateTime getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	@Override
	public String toString() {
		return "GiftCertificateGetDTO [id=" + id + ", name=" + name + ", description=" + description + ", price="
				+ price + ", duration=" + duration + ", creationDate=" + creationDate + ", lastUpdateDate="
				+ lastUpdateDate + "]";
	}

}
