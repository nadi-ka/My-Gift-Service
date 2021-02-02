package com.epam.esm.audit;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@Embeddable
public class Audit {
	
	@Column(name = "Created_on")
	private LocalDateTime createdOn;
	
	@Column(name = "Updated_on")
	private LocalDateTime updatedOn;

	@PrePersist
    public void prePersist() {
		createdOn = createAndformatDateTime();
	}
	
	@PreUpdate
	public void preUpdate() {
		updatedOn = createAndformatDateTime();
	}
	
	public LocalDateTime getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}

	public LocalDateTime getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(LocalDateTime updatedOn) {
		this.updatedOn = updatedOn;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((createdOn == null) ? 0 : createdOn.hashCode());
		result = prime * result + ((updatedOn == null) ? 0 : updatedOn.hashCode());
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
		Audit other = (Audit) obj;
		if (createdOn == null) {
			if (other.createdOn != null)
				return false;
		} else if (!createdOn.equals(other.createdOn))
			return false;
		if (updatedOn == null) {
			if (other.updatedOn != null)
				return false;
		} else if (!updatedOn.equals(other.updatedOn))
			return false;
		return true;
	}

	private LocalDateTime createAndformatDateTime() {
		String formatPattern = "yyyy-MM-dd HH:mm:ss";
		String zone = "Europe/Minsk";
	    ZonedDateTime zoneEuropeMinsk = ZonedDateTime.now(ZoneId.of(zone));
	    LocalDateTime ldt = zoneEuropeMinsk.toLocalDateTime();
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatPattern);
	    zoneEuropeMinsk.format(formatter);
	    return ldt;
	}

}
