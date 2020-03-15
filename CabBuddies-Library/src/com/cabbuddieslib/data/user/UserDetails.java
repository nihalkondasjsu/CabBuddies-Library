package com.cabbuddieslib.data.user;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.apache.commons.lang3.Validate;

import com.cabbuddieslib.reference.Messages;

@Entity
public class UserDetails {

	public enum UserRegistrationType{
		InApp,Facebook,Google
	}
	
	@Id
	@GeneratedValue
	@Basic(optional = false)
	private Long id;
	
	@Column(unique=true,nullable=false)
	private String email;
	
	@Column(nullable=false)
	private String password;
	
	private String firstName;
	
	private String lastName;
	
	private String dp;
	
	@Enumerated(EnumType.STRING)
	private UserRegistrationType userRegistrationType = UserRegistrationType.InApp;
	
	@JoinColumn(updatable=false)
	@OneToOne(fetch=FetchType.EAGER,
			cascade=CascadeType.ALL,
			orphanRemoval=true)
	private UserVerifiedDetails verifiedDetails;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public String getDp() {
		return dp;
	}

	public void setDp(String dp) {
		this.dp = dp;
	}

	public UserRegistrationType getUserRegistrationType() {
		return userRegistrationType;
	}

	public void setUserRegistrationType(UserRegistrationType userRegistrationType) {
		this.userRegistrationType = userRegistrationType;
	}

	public UserVerifiedDetails getVerifiedDetails() {
		return verifiedDetails;
	}

	public void setVerifiedDetails(UserVerifiedDetails verifiedDetails) {
		this.verifiedDetails = verifiedDetails;
	}
	
	public void prepareForRegistration() {
		Validate.notEmpty(email, Messages.Validation.MISSING_PROPERTY+"Email.");
		Validate.notEmpty(password, Messages.Validation.MISSING_PROPERTY+"Password.");
		Validate.notEmpty(firstName, Messages.Validation.MISSING_PROPERTY+"First Name.");
		Validate.notEmpty(lastName, Messages.Validation.MISSING_PROPERTY+"Last Name.");
	}
	
	public void prepareForLogin() {
		Validate.notEmpty(email, Messages.Validation.MISSING_PROPERTY+"Email.");
		Validate.notEmpty(password, Messages.Validation.MISSING_PROPERTY+"Password.");
	}
}
