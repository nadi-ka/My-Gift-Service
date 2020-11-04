package com.epam.esm.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Order")
public class Order {

	@Id
	@Column(name="Id")
	private long id;
	
	@Column(name="Id_user")
	private long userId;
	
	@Column(name="Creation_date")
	private LocalDateTime creationDate;
	
	private List<GiftCertificate> certificates;
	
	public Order() {}

	public Order(long userId, LocalDateTime creationDate, List<GiftCertificate> certificates) {
		this.userId = userId;
		this.creationDate = creationDate;
		this.certificates = certificates;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((certificates == null) ? 0 : certificates.hashCode());
		result = prime * result + ((creationDate == null) ? 0 : creationDate.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + (int) (userId ^ (userId >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Order other = (Order) obj;
		if (certificates == null) {
			if (other.certificates != null)
				return false;
		} else if (!certificates.equals(other.certificates))
			return false;
		if (creationDate == null) {
			if (other.creationDate != null)
				return false;
		} else if (!creationDate.equals(other.creationDate))
			return false;
		if (id != other.id)
			return false;
		if (userId != other.userId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", userId=" + userId + ", creationDate=" + creationDate + ", certificates="
				+ certificates + "]";
	}

}
