package com.epam.esm.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.epam.esm.audit.Audit;

@Entity
@Table(name="User", schema = "Certificate_service")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="Id")
	private long id;
	
	@Column(name="First_name")
	private String firstName;
	
	@Column(name="Last_name")
	private String lastName;
	
	@Column(name="Date_of_birth")
	private LocalDate dateOfBirth;
	
	@Column(name="Email")
	private String email;
	
//	private String login;
//	
//    private String password;
//    
//    private Role role;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	private List<Purchase> purchases;
	
	@Embedded
	private Audit audit = new Audit(); 
	
	public User() {}
	
	public User(Long id) {
		this.id = id;
	}

	public User(String firstName, String lastName, LocalDate dateOfBirth, String email) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
		this.email = email;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Purchase> getOrders() {
		return purchases;
	}

	public void setOrders(List<Purchase> orders) {
		this.purchases = orders;
	}
	
	public void addOrder(Purchase theOrder) {
		
		if (purchases == null) {
			purchases = new ArrayList<Purchase>();
		}
		purchases.add(theOrder);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dateOfBirth == null) ? 0 : dateOfBirth.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((purchases == null) ? 0 : purchases.hashCode());
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
		User other = (User) obj;
		if (dateOfBirth == null) {
			if (other.dateOfBirth != null)
				return false;
		} else if (!dateOfBirth.equals(other.dateOfBirth))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (id != other.id)
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (purchases == null) {
			if (other.purchases != null)
				return false;
		} else if (!purchases.equals(other.purchases))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", dateOfBirth=" + dateOfBirth
				+ ", email=" + email + "]";
	}

}
