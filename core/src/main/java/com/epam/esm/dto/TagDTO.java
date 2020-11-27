package com.epam.esm.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.hateoas.RepresentationModel;

public class TagDTO extends RepresentationModel<TagDTO> {

	private long id;

	@NotBlank
	@Size(max = 45, message = "must be less or equal to 45")
	private String name;

	public TagDTO() {
	}

	public TagDTO(long id, String name) {
		this.id = id;
		this.name = name;
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

	@Override
	public String toString() {
		return "TagDTO [id=" + id + ", name=" + name + "]";
	}

}
