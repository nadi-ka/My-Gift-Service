package com.epam.esm.dto;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class GiftCertificateCreateDTO {

	private long id;

	@NotBlank
	@Size(max = 45)
	private String name;

	@NotBlank
	@Size(max = 100)
	private String description;

	@NotNull
	@DecimalMin("0.1")
	@DecimalMax("10000.0")
	private BigDecimal price;

	@NotNull
	@Min(value = 1)
	@Max(value = 1000)
	private Integer duration;

	private List<TagDTO> tags;

	public GiftCertificateCreateDTO() {
	}

	public GiftCertificateCreateDTO(long id, @NotBlank @Size(max = 45) String name,
			@NotBlank @Size(max = 100) String description,
			@NotNull @DecimalMin("0.1") @DecimalMax("10000.0") BigDecimal price,
			@NotNull @Min(value = 1) @Max(value = 1000) Integer duration, List<TagDTO> tags) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.duration = duration;
		this.tags = tags;
	}

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

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
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

	@Override
	public String toString() {
		return "GiftCertificateCreateDTO [id=" + id + ", name=" + name + ", description=" + description + ", price="
				+ price + ", duration=" + duration + ", tags=" + tags + "]";
	}

}
