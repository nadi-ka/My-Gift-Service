package com.epam.esm.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonFormat;

public class PurchaseDTO extends RepresentationModel<PurchaseDTO> {

	private long id;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Europe/Minsk")
	private LocalDateTime creationDate;

	private BigDecimal cost;

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

	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	@Override
	public String toString() {
		return "PurchaseDTO [id=" + id + ", creationDate=" + creationDate + ", cost=" + cost + "]";
	}

}
