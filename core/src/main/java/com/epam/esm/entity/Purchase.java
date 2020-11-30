package com.epam.esm.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="Purchase")
public class Purchase {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="Id")
	private long id;
	
	@Column(name="Creation_date")
	private LocalDateTime creationDate;
	
	@Column(name="Cost")
	private BigDecimal cost;
	
	@ManyToOne
	@JoinColumn(name = "Id_user")
	private User user;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "Purchase_Certificate", schema = "Certificate_service", 
	joinColumns = {@JoinColumn(name = "Id_order")}, 
	inverseJoinColumns = {@JoinColumn(name = "Id_certificate")})
	private List<GiftCertificate> certificates;
	
	public Purchase() {}

	public Purchase(LocalDateTime creationDate, User user, List<GiftCertificate> certificates) {
		this.creationDate = creationDate;
		this.user = user;
		this.certificates = certificates;
	}

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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	public void addCertificate(GiftCertificate certificate) {
		
		if (certificates == null) {
			certificates = new ArrayList<GiftCertificate>();
		}
		certificates.add(certificate);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((certificates == null) ? 0 : certificates.hashCode());
		result = prime * result + ((cost == null) ? 0 : cost.hashCode());
		result = prime * result + ((creationDate == null) ? 0 : creationDate.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((user == null) ? 0 : user.hashCode());
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
		Purchase other = (Purchase) obj;
		if (certificates == null) {
			if (other.certificates != null)
				return false;
		} else if (!certificates.equals(other.certificates))
			return false;
		if (cost == null) {
			if (other.cost != null)
				return false;
		} else if (!cost.equals(other.cost))
			return false;
		if (creationDate == null) {
			if (other.creationDate != null)
				return false;
		} else if (!creationDate.equals(other.creationDate))
			return false;
		if (id != other.id)
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Purchase [id=" + id + ", creationDate=" + creationDate + ", cost=" + cost + "]";
	}

}
